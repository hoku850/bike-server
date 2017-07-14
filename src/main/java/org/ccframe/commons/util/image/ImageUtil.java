package org.ccframe.commons.util.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.ccframe.commons.util.BusinessException;

import com.sun.imageio.plugins.jpeg.JPEGImageWriter;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;

/**
 * 图片工具.
 * 包含一系列工具方法封装，例如使用Jai处理PNG等
 * @author Jim
 *
 * TODO 有空研究下sanselan，看看生成JPG速度谁高
 */
public class ImageUtil {

    public static final  String IMAGE_TYPE_BMP = "BMP";
    public static final String IMAGE_TYPE_GIF = "GIF";
    public static final String IMAGE_TYPE_JPEG = "JPEG";
    public static final String IMAGE_TYPE_JPG = "JPG";
    public static final String IMAGE_TYPE_PNG = "PNG";
    public static final String IMAGE_TYPE_PNM = "PNM";
    public static final String IMAGE_TYPE_TIFF = "TIFF";
    public static final String ERROR_MSG_SOURCE_IMAGE_FILE_NO_EXIST = "source Image File not exist!";
    private static Logger logger = Logger.getLogger(ImageUtil.class.getName());
    static {
        System.setProperty("com.sun.media.jai.disableMediaLib", "true");
    }

    public static class Size{
    	private int width;
    	private int height;
    	public Size(int width, int height){
    		this.width = width;
    		this.height = height;
    	}
		public int getWidth() {
			return width;
		}
		public void setWidth(int width) {
			this.width = width;
		}
		public int getHeight() {
			return height;
		}
		public void setHeight(int height) {
			this.height = height;
		}
    	
    }
    
    private ImageUtil(){}
    /**
     * 改变图片大小
     *
     *@param img Image 源图片
     * @param width int 宽
     * @param height int 高
     * @throws Exception
     * @return BufferedImage
     */
    private static Image changSize(Image img, int width, int height) {
        int w = img.getWidth(null);
        int h = img.getHeight(null);

//        if (w > width || h > height){
        if (w == h) {
            //宽度和高度，以小的为标准
            if (width >= height) {
                h = height;
                w = h;
            } else {
                w = width;
                h = w;
            }
        } else if (w > h) {
            float scale = (float) h / w;  //计得小于1的小数
            w = width;
            h = (int) (w * scale);       //按比例计算
        } else {
            float scale = (float) w / h;
            h = height;
            w = (int) (h * scale);
        }
//        }

        img = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return img;
    }

    /**
     * 得到图片的缓冲图像
     *
     *@param img Image 源图片
     * @return BufferedImage
     */
    private static BufferedImage toBufferedImage(Image img){
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setBackground(Color.WHITE);
        g.fillRect(0, 0, w, h);

        bi.getGraphics().drawImage(img, 0, 0, w, h, null);
        return bi;
    }

    public static BufferedImage toCroppedImage(File file,int x,int y,int w,int h) throws IOException {
        BufferedImage outImage=ImageIO.read(file);
        BufferedImage cropped=outImage.getSubimage(x, y, w, h);
        return cropped;

    }

    /**
     * 处理jpeg格式的图片  把调整好大小的图片进行转换并输出
     *
     * @param bi BufferedImage 源文件 的 图像数据
     * @param dest File 目的文件
     * @throws Exception
     */
    private static void toJPEG(BufferedImage bi, File dest) throws IOException {
        JPEGImageWriter imageWriter = (JPEGImageWriter) ImageIO.getImageWritersBySuffix("jpeg").next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(dest);
        imageWriter.setOutput(ios);

        JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter.getDefaultWriteParam();
/*
*        setCompressionMode:
*        指定是否执行压缩，如果执行压缩，将如何确定 compression 参数。mode 参数必须是四种模式之一，对它们的解释如下：
*        MODE_DISABLED - 如果该模式被设置为 MODE_DISABLED，则查询或修改压缩类型或参数的方法将会抛出 IllegalStateException（如果插件通常支持压缩）。
*        一些 writer（比如 JPEG）通常不提供未压缩的输出。在这种情况下，试图将模式设置为 MODE_DISABLED 将会抛出 UnsupportedOperationException 并且将不更改该模式。
*        MODE_EXPLICIT - 使用此 ImageWriteParam 中指定的压缩类型和质量设置进行压缩。任何以前设置的 compression 参数都将被丢弃。
*        MODE_COPY_FROM_METADATA - 使用传入 writer 的元数据对象中指定的 compression 参数。
*        MODE_DEFAULT - 使用默认 compression 参数。
*        默认值为 MODE_COPY_FROM_METADATA。
*/
        jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
/*
*        setCompressionQuality :
*        将压缩质量设置为 0 和 1 之间的某个值。默认情况下，只支持一种压缩质量设置；writer 可以提供扩展的 ImageWriteParam，其提供了更多控制。
*        对于有损失的压缩方案，压缩质量应该控制文件大小与图像质量之间的权衡（例如，通过在写入 JPEG 图像时选择量化表）。
*        对于无损失方案，可以使用压缩质量控制文件大小与执行压缩所用时间之间的权衡（例如，通过在写入 PNG 图像时优化行过滤器并设置 ZLIB 压缩级别）。
*       压缩质量为 0.0 通常被解释为“高度压缩很重要”，而该设置为 1.0 通常被解释为“高图像质量很重要”。
*/

        // jpegParams.setCompressionQuality(0.95f);
        jpegParams.setCompressionQuality(1.00f);
        IIOMetadata data = imageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(bi), jpegParams);

        imageWriter.write(data, new IIOImage(bi, null, null), jpegParams);
        ios.close();
        imageWriter.dispose();
    }

    /**
     * 处理gif格式的图片  把调整好大小的图片进行转换并输出 // gif to 静态的图片
     *
     * @param bi BufferedImage 源文件 的 图像数据
     * @param dest File 目的文件
     * @throws Exception
     */
//    private void toStaticGIF(BufferedImage bi, File dest) throws Exception{
//        AnimatedGifEncoder age = new AnimatedGifEncoder();
//        age.setQuality(1);
//        age.start(dest.toString());
//        age.addFrame(bi);
//        age.finish();
//    }

    /**
     * 处理gif格式的图片  把调整好大小的图片进行转换并输出 // gif to 动态的图片
     *
     * @param src File 源文件
     * @param dest File 目的文件
     * @param width int 宽
     * @param height int 高
     * @throws Exception
     */
    private static void toActiveGIF(File src, File dest, int width, int height) {
        //把gif图片 按帧拆分成jpg图片
        GifDecoder decoder = new GifDecoder();
        decoder.read(src.getPath());
        int n = decoder.getFrameCount(); //得到frame的个数
        ArrayList<BufferedImage> biList = new ArrayList<BufferedImage>();
        ArrayList<Integer> delayList = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            BufferedImage bi = decoder.getFrame(i); //得到帧
            //调整大小
            Image  img = changSize(bi, width, height);
            bi = toBufferedImage(img);
            biList.add(bi);
            int delay = decoder.getDelay(i); //得到延迟时间
            delayList.add(delay);
        }

        //多张jpg图合成gif动画
        AnimatedGifEncoder e = new AnimatedGifEncoder();
        e.setRepeat(0);
        e.start(dest.getPath());
        for (int i = 0; i < biList.size(); i++) {
            e.setDelay(delayList.get(i)); //设置播放的延迟时间
            e.addFrame(biList.get(i));  //添加到帧中
        }
        e.finish();
    }

    /**
     * 处理png格式的图片  把调整好大小的图片进行转换并输出
     *
     //* @param bi BufferedImage 源文件 的 图像数据
     * @param dest File 目的文件
     * @throws Exception
     */
    private static void toPNG(File src, File dest, int width, int height) throws IOException {
        Image  img = changSize(ImageIO.read(src), width, height);
        PlanarImage pi = loadImage(src.getPath(),img.getWidth(null),img.getHeight(null));
        FileOutputStream fout = new FileOutputStream(dest.getPath());
        encodeImage(pi, fout);
    }

    /* Load the source image. */
    private static PlanarImage loadImage(String imageName,int width, int height) throws IOException {
        /**
         * Create an input stream from the specified file name to be used with
         * the file decoding operator.
         */
        FileSeekableStream stream = new FileSeekableStream(imageName);

        /* Create an operator to decode the image file. */
        RenderedOp image1 = JAI.create("stream", stream);
        int sourceImageWidth = image1.getWidth();
        int sourceImageHeight = image1.getHeight();

        /**
         * Create a standard bilinear interpolation object to be used with the
         * "scale" operator.
         */

        Interpolation  interpolation = Interpolation.getInstance(Interpolation.INTERP_NEAREST); // Interpolation.INTERP_BILINEAR
        /**
         * Stores the required input source and parameters in a ParameterBlock
         * to be sent to the operation registry, and eventually to the "scale"
         * operator.
         */
        ParameterBlock params = new ParameterBlock();
        params.addSource(image1);
        float xscale = (float) width / sourceImageWidth;
        float yscale = (float) height / sourceImageHeight;

        params.add(new Float(xscale));
        params.add(new Float(yscale));
        params.add(new Float(0.0f));
        params.add(new Float(0.0f));
        params.add(interpolation);
        /* Create an operator to scale image1. */
        PlanarImage src = JAI.create("scale", params);
        return src;
    }

    // Create the image encoder.
    private static void encodeImage(PlanarImage img, FileOutputStream out) throws IOException {
        ImageEncoder encoder = ImageCodec.createImageEncoder("png", out, null);
        encoder.encode(img);
        out.close();
    }


    /**
     * 处理其他格式的图片  把调整好大小的图片进行转换并输出
     *
     * @param bi BufferedImage 源文件 的 图像数据
     * @param dest File 目的文件
     * @throws Exception
     */
    private static void toOther(String imageType, BufferedImage bi, File dest) throws IOException {
        ImageIO.write(bi, imageType, dest);
    }

    /**
     * 改变图片大小
     *
     * @param imageType 图片类型,支持的图片类型在常量里定义
     * @param src File 源文件
     * @param dest File 目的文件
     * @param width int 宽
     * @param height int 高
     * @throws Exception
     */
    public static boolean doChangeSize(String imageType, File src, File dest, int width, int height) throws IOException {
//        AssertExt.hasLength(imageType);
//        AssertExt.isTrue((src != null && src.exists()), ERROR_MSG_SOURCE_IMAGE_FILE_NO_EXIST);
//        AssertExt.notNull(dest);
//        AssertExt.isTrue(width > 0 && height > 0);
//        AssertExt.hasLength(imageType);
        //处理gif图片
        if(imageType.equalsIgnoreCase(IMAGE_TYPE_GIF)){
//            toStaticGIF(bi, dest);
            toActiveGIF(src, dest, width, height);
            return true;
        }

        //处理其他图片
        Image img = ImageIO.read(src);
        img = changSize(img, width, height);
        BufferedImage bi = toBufferedImage(img);
        if (imageType.equalsIgnoreCase(IMAGE_TYPE_JPEG) || imageType.equalsIgnoreCase(IMAGE_TYPE_JPG)) {
            toJPEG(bi, dest);
            return true;
        }
        if(imageType.equalsIgnoreCase(IMAGE_TYPE_PNG)){
            toPNG(src, dest, width, height);
            return true;
        }
        toOther(imageType, bi, dest);
        return true;
    }


    public static boolean doChangeSize(String imageType, File src, File dest, int width, int height,int x,int y) throws IOException {
//        AssertExt.hasLength(imageType);
//        AssertExt.isTrue((src != null && src.exists()), ERROR_MSG_SOURCE_IMAGE_FILE_NO_EXIST);
//        AssertExt.notNull(dest);
//        AssertExt.isTrue(width > 0 && height > 0);
//        AssertExt.hasLength(imageType);
        //处理gif图片
        if(imageType.equalsIgnoreCase(IMAGE_TYPE_GIF)){
//            toStaticGIF(bi, dest);
            toActiveGIF(src, dest, width, height);
            return true;
        }

        //处理其他图片
        Image img = ImageIO.read(src);
        // img = changSize(img, width, height);
        BufferedImage bi = toCroppedImage(src,x,y,width,height);
        if (imageType.equalsIgnoreCase(IMAGE_TYPE_JPEG)) {
            toJPEG(bi, dest);
            return true;
        }
        if(imageType.equalsIgnoreCase(IMAGE_TYPE_PNG)){
            toPNG(src, dest, width, height);
            return true;
        }
        toOther(imageType, bi, dest);
        return true;
    }

	/**
	 * TODO 前端也需要使用的要整理。
	 * @param url
	 * @param suffix
	 * @return
	 */
	public static String insertFileNameSuffixToUrl(String url, String suffix){
		int index = url.lastIndexOf('.');
		if(index > 0){
			StringBuilder sb = new StringBuilder(url);
			sb.insert(index, suffix);
			return sb.toString();
		}
		return url;
	}

    public static Map<String,File> genPictures(Size size,File src) throws IOException {
    	List<Size> sizes = new ArrayList<Size>();
    	sizes.add(size);
    	return genPictures(sizes, src, null);
    }

	public static Map<String,File> genPictures(List<Size> sizes,File src) throws IOException {
    	return genPictures(sizes, src, null);
    }

    /**
     * 生成小图.
//     * @param config
     * @param src
     * @param destDir 如果为null，就在src同目录下生成
     * @return
     * @throws IOException
     */
    public static Map<String,File> genPictures(List<Size> sizes,File src,File destDir) throws IOException {
        if(src == null || !src.exists()){
            //source Image File not exist!
            logger.error(ERROR_MSG_SOURCE_IMAGE_FILE_NO_EXIST);
            return new HashMap<String,File>();
        }
//        AssertExt.notNull(sizes);

        File realDestDir = null;
        if(destDir!=null){
            realDestDir = destDir;
            if(!destDir.exists()){
//                AssertExt.isTrue(destDir.mkdirs());
            }
        }else {
            realDestDir = src.getParentFile();
        }

        String fileName = src.getName();
        HashMap<String,File> files = new HashMap<String,File>();
        Image img = ImageIO.read(src);
        int srcWidth = img.getWidth(null);
        int srcHeight = img.getHeight(null);
        for(Size size: sizes){
            String sizeStr = size.width + "x" + size.height;
            File newFile = new File(realDestDir,insertFileNameSuffixToUrl(fileName, "_" + sizeStr));
            int width = size.width;
            int height = size.height;
            if(srcWidth < width && srcHeight < height){
                //如果原图大小比规格小，那么取原图大小
                width = srcWidth;
                height = srcHeight;
            }
            doChangeSize(FilenameUtils.getExtension(fileName),src,newFile,width,height);
            files.put(sizeStr,newFile);
        }
        return files;
    }

    /**
     * 添加图片水印
     *
     * @param file  目标图片
     * @param x     水印图片距离目标图片左侧的偏移量，如果x<0, 则在正中间
     * @param y     水印图片距离目标图片上侧的偏移量，如果y<0, 则在正中间
     * @param alpha 透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     * @throws BusinessException 异常
     */
/*
    public static void pressImage(File file,int x, int y, float alpha) throws BusinessException {
        try {
            // 加载目标图片
            if(!file.exists()){
                return;
            }
            String fileName = file.getName();
            String extention = fileName.substring(fileName.lastIndexOf("."));
            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);

            // 将目标图片加载到内存。
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, width, height, null);

            // 加载水印图片。？？？怎么把ServiceManager也引入了
            String waterMark = ServiceManager.paramInfService.getByInnerCode(Global.WATER_MARK).getParamValueStr();
            if (StringUtils.isBlank(waterMark)) {
                return;
            }
            Image waterImage = ImageIO.read(new File(WebContextFactory.getWebRealPath() + File.separatorChar + Global.UPLOAD_DIR + File.separatorChar + waterMark));
            int width_1 = waterImage.getWidth(null);
            int height_1 = waterImage.getHeight(null);
            // 设置水印图片的透明度。
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

            // 设置水印图片的位置。
            int waterX = width - width_1 - x;
            int waterY = height - height_1 - y;
            if (x < 0) {
                waterX = (width - width_1) / 2;
            }
            if (y < 0) {
                waterY = (height - height_1) / 2;
            }

            // 将水印图片“画”在原有的图片的制定位置。
            g.drawImage(waterImage, waterX, waterY, width_1, height_1, null);
            // 关闭画笔。
            g.dispose();

            // 保存目标图片。
            ImageIO.write(bufferedImage, extention.substring(1), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
    /**
     * 添加文字水印
     *
     * @param targetImg 目标图片路径，如：C:\\kutoku.jpg
     * @param pressText 水印文字， 如：kutuku.com
     * @param fontName  字体名称， 如：宋体
     * @param fontStyle 字体样式，如：粗体和斜体(Font.BOLD|Font.ITALIC)
     * @param fontSize  字体大小，单位为像素
     * @param color     字体颜色
     * @param x         水印文字距离目标图片左侧的偏移量，如果x<0, 则在正中间
     * @param y         水印文字距离目标图片上侧的偏移量，如果y<0, 则在正中间
     * @param alpha     透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     */
    public static void pressText(String targetImg, String pressText,
                                 String fontName, int fontStyle, int fontSize, Color color, int x,
                                 int y, float alpha) {
        try {
            // 加载目标图片
            File file = new File(targetImg);
            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);

            // 将目标图片加载到内存。
            BufferedImage bufferedImage = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, width, height, null);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setColor(color);
            // 设置水印图片的透明度。
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

            // 设置水印图片的位置。
            int width_1 = fontSize * pressText.getBytes("GBK").length;
            int height_1 = fontSize;
            int widthDiff = width - width_1;
            int heightDiff = height - height_1;
            if (x < 0) {
                x = widthDiff / 2;
            } else if (x > widthDiff) {
                x = widthDiff;
            }
            if (y < 0) {
                y = heightDiff / 2;
            } else if (y > heightDiff) {
                y = heightDiff;
            }

            // 将水印文字“写”在原有的图片的制定位置。
            g.drawString(pressText, x, y + height_1);
            // 关闭画笔。
            g.dispose();

            String fileName = file.getName();
            String fnm = fileName.substring(0, fileName.lastIndexOf('.'));
            String extention = fileName.substring(fileName.lastIndexOf("."));
            // 保存目标图片。
            ImageIO.write(bufferedImage, extention.substring(1), new File("F://sy//" + fnm + "_text" + extention));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
