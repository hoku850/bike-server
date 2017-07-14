package org.ccframe.commons.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.ccframe.commons.data.PdfCellPrintData.BorderMode;
import org.ccframe.commons.data.PdfCellPrintData.VerticalAlign;
import org.ccframe.commons.data.PdfPrintData.HorizontalAlign;
import org.ccframe.commons.util.BusinessException;
import org.ccframe.commons.util.WebContextHolder;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfLayer;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * pdf打印及批量打印工具
 * @author Jim
 *
 */
public class PdfBatchPrintWriter{

	public static enum Zoom{
		/**
		 * 默认无缩放
		 */
		DEFAULT, 
		/**
		 * 扫描按照屏幕分辨率96dpi，而pdf为72dpi，屏幕大小到pdf位置的换算比为0.75，如果套打要按PX对齐则需要乘上此系数
		 */
		ZOOM_96_TO_72_DPI
	}
	private float pageHeight;
	private float zoomf;

	private Document document;
	private PdfWriter prdWriter;
	private PdfContentByte cb;
	private FileOutputStream outputStream;

	private PdfLayer notPrintedLayer; 
	private PdfLayer frontLayer; 

	private Image backgroundImg;
	
	private static BaseFont bfChinese;   

    public PdfBatchPrintWriter(float pageWidth, float pageHeight, String outputPath, String backgroundPath, Zoom zoom){
    	try {
    		String fontPath = WebContextHolder.getWarPath() + File.separator + "WEB-INF" + File.separator + "YaHei_MavenPro_emoji.ttf"; 
    		bfChinese = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    		this.pageHeight = pageHeight;
    		zoomf = (zoom == Zoom.ZOOM_96_TO_72_DPI ? 0.75f : 1.0f);
            document = new Document(new Rectangle(0, 0, pageWidth * zoomf, pageHeight * zoomf), 20f, 20f, 20f, 20f);
    		
            File parentFile = new File(outputPath).getParentFile();
            if (!parentFile.exists()){
                parentFile.mkdirs();
            }
            outputStream = new FileOutputStream(outputPath);
            prdWriter = PdfWriter.getInstance(document, outputStream);

            document.open();
            notPrintedLayer = new PdfLayer("not printed", prdWriter);
			notPrintedLayer.setOnPanel(false); 
			notPrintedLayer.setPrint("Print", false); 
			
			frontLayer = new PdfLayer("printed", prdWriter); 
			frontLayer.setOnPanel(false); 
			frontLayer.setPrint("Print", true);
			
			if(backgroundPath != null){
	    		backgroundImg = Image.getInstance(backgroundPath);
	    		backgroundImg.scalePercent(zoomf * 100); 
	    		backgroundImg.setAbsolutePosition(0, 0); 
			}
		} catch (IOException e) {
			throw new BusinessException("pdf craete document failed.", e);
		} catch (DocumentException e) {
			throw new BusinessException("pdf craete document failed.", e);
		}
        cb = prdWriter.getDirectContent();
    }

    /**
     * 按照格式进行套打一个页面
     * @param pdfAreaPrintDatas
     */
    public PdfBatchPrintWriter floatLayerPrint(Iterable<PdfAreaPrintData> pdfAreaPrintDatas){
    	try {
	    	document.newPage();
	    	if(backgroundImg != null){ //生成套打背景
	    		cb.beginLayer(notPrintedLayer);
	    		cb.addImage(backgroundImg); 
	    		cb.endLayer();
	    	}
	    	cb.beginLayer(frontLayer);
	    	for(PdfAreaPrintData pdfAreaPrintData: pdfAreaPrintDatas){
	        	float x = pdfAreaPrintData.getLeft()*zoomf;
	        	float y = (pageHeight - pdfAreaPrintData.getTop())*zoomf;
	        	float width = pdfAreaPrintData.getWidth()*zoomf;
	        	float height = pdfAreaPrintData.getHeight()*zoomf;
	        	int fontSize = Double.valueOf(Math.round(pdfAreaPrintData.getFontSize() * zoomf)).intValue();
	        	
	        	Font fontChinese = new Font(bfChinese, fontSize, Font.NORMAL);
	        	if(pdfAreaPrintData.isBold()){
	        		fontChinese.setStyle(Font.BOLD);
	        	}
	        	if(pdfAreaPrintData.isItalic()){
	        		fontChinese.setStyle(Font.ITALIC);
	        	}
	        	if(pdfAreaPrintData.getFontspace() > 0){
	        		cb.setCharacterSpacing(pdfAreaPrintData.getFontspace());
	        	}

	        	ColumnText ct = new ColumnText(cb);
	        	Phrase p = new Phrase(pdfAreaPrintData.getValue(), fontChinese);
	        	p.add(Chunk.NEWLINE);
	        	int align = Element.ALIGN_LEFT;
	        	switch(pdfAreaPrintData.getHorizontalAlign()){
	        		case LEFT:
		        		align = Element.ALIGN_LEFT;
		        		break;
	        		case RIGHT:
		        		align = Element.ALIGN_RIGHT;
		        		break;
	        		case CENTER:
		        		align = Element.ALIGN_CENTER;
	        		break;
	        	}
	        	ct.setSimpleColumn(p, x, y, x+width, y-height, fontSize + 2, align);
	        	ct.go();
	    	}
	    	cb.endLayer();
		} catch (DocumentException e) {
			throw new BusinessException("pdf craete document failed.", e);
		} finally {
	    	return this;
		}
    }

    public PdfBatchPrintWriter gridLayerPrint(Iterable<PdfCellPrintData> pdfCellPrintDatas, float... columns){
    	try {
	    	document.newPage();
	    	PdfPTable table = new PdfPTable(columns);

	    	table.setWidthPercentage(100);

	    	for(PdfCellPrintData pdfCellPrintData: pdfCellPrintDatas){
	        	Font fontChinese = new Font(bfChinese, pdfCellPrintData.getFontSize(), Font.NORMAL);
	        	if(pdfCellPrintData.isBold()){
	        		fontChinese.setStyle(Font.BOLD);
	        	}
	        	if(pdfCellPrintData.isItalic()){
	        		fontChinese.setStyle(Font.ITALIC);
	        	}
	        	if(pdfCellPrintData.getFontspace() > 0){
//	        		cb.setCharacterSpacing(pdfCellPrintData.getFontspace());
	        	}

	    		PdfPCell pdfPCell = new PdfPCell(new Paragraph(pdfCellPrintData.getValue(),fontChinese));
	    		pdfPCell.setPaddingTop(pdfCellPrintData.getTopBottomPadding());
	    		pdfPCell.setPaddingRight(pdfCellPrintData.getLeftRightPadding());
	    		pdfPCell.setPaddingBottom(pdfCellPrintData.getTopBottomPadding());
	    		pdfPCell.setPaddingLeft(pdfCellPrintData.getLeftRightPadding());
	    		
	    		pdfPCell.setHorizontalAlignment(convertHorizontalAlignToInt(pdfCellPrintData.getHorizontalAlign()));
	    		pdfPCell.setVerticalAlignment(convertVerticalAlignToInt(pdfCellPrintData.getVerticalAlign()));
	    		if(pdfCellPrintData.getHeight() != null){
	    			pdfPCell.setFixedHeight(pdfCellPrintData.getHeight());
	    		}
	    		if(pdfCellPrintData.getRowspan() != null){
	    			pdfPCell.setRowspan(pdfCellPrintData.getRowspan());
	    		}
	    		if(pdfCellPrintData.getColspan() != null){
	    			pdfPCell.setColspan(pdfCellPrintData.getColspan());
	    		}
	    		renderBorderMode(pdfCellPrintData.getBorderMode(), pdfPCell);
	    		
		    	table.addCell(pdfPCell);
	    	}
	    	document.add(table);
		} finally {
			return this;
		}
    }

    private void renderBorderMode(BorderMode borderMode, PdfPCell pdfPCell) {
    	if(borderMode == BorderMode.NONE){
            pdfPCell.setBorder(Rectangle.NO_BORDER);
    		return;
    	}
    	if(borderMode == BorderMode.THIN){
    		pdfPCell.setBorderWidth(0.5f);
    		return;
    	}
    	if(borderMode == BorderMode.THICK){
    		pdfPCell.setBorderWidth(2f);
    		return;
    	}
    	if(borderMode == BorderMode.BOTTOM_THIN){
    		pdfPCell.setBorderWidth(Rectangle.NO_BORDER);
    		pdfPCell.setBorderWidthBottom(0.5f);
    		return;
    	}
    	if(borderMode == BorderMode.BOTTOM_THICK){
    		pdfPCell.setBorderWidth(Rectangle.NO_BORDER);
    		pdfPCell.setBorderWidthBottom(2f);
    		return;
    	}
    	if(borderMode == BorderMode.BOTTOM_DASH){
            pdfPCell.setBorder(Rectangle.NO_BORDER);
            pdfPCell.setCellEvent(new PdfPCellEvent(){
				@Override
				public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] contentByte) {
		            PdfContentByte cb = contentByte[PdfPTable.TEXTCANVAS];
		            cb.setLineCap(PdfContentByte.LINE_CAP_PROJECTING_SQUARE);
		            cb.setLineDash(2, 0);
		            cb.setLineWidth(0.5f);
		            cb.moveTo(rect.getLeft(), rect.getBottom());
		            cb.lineTo(rect.getRight(), rect.getBottom());
		            cb.stroke();
				}
            });
    	}
	}

	private int convertHorizontalAlignToInt(HorizontalAlign horizontalAlign){
    	switch(horizontalAlign){
    		case LEFT:
    			return Element.ALIGN_LEFT;
    		case CENTER: 
    			return Element.ALIGN_CENTER;
    		case RIGHT: 
    			return Element.ALIGN_RIGHT;
    		default:
    			return Element.ALIGN_LEFT;
    	}
    }

    private int convertVerticalAlignToInt(VerticalAlign verticalAlign){
    	switch(verticalAlign){
		case TOP:
			return Element.ALIGN_TOP;
		case MIDDLE: 
			return Element.ALIGN_MIDDLE;
		case BOTTOM: 
			return Element.ALIGN_BOTTOM;
		default:
			return Element.ALIGN_MIDDLE;
	}
    }

    public void endPage(){
        document.close();
        IOUtils.closeQuietly(outputStream);
    }
   
}
