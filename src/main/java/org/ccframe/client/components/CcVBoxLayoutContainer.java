package org.ccframe.client.components;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Size;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;

/**
 * 
 * 修复VerticalLayout的自动高度计算问题.
 * 解决办法：1.清理上级高度 2.每次都重新计算整体高度（支持对象隐藏）。
 * 如果要自动撑开窗体，还是用VBoxLayoutContainer
 * 
 * @author JIM
 *
 */
public class CcVBoxLayoutContainer extends VBoxLayoutContainer {
	
	private boolean autoHeight = true;
	
	public boolean isAutoHeight() {
		return autoHeight;
	}

	public void setAutoHeight(boolean autoHeight){
		this.autoHeight = autoHeight;
	}
	
	@Override
	protected void doLayout() {
	    Size size = getElement().getSize();

	    int w = size.getWidth() - getScrollOffset();
	    int h = size.getHeight();
	    
	    int styleWidth = Util.parseInt(getElement().getStyle().getProperty("width"), Style.DEFAULT);

	    boolean findWidth = styleWidth == -1;

	    int calculateHeight = 0;

	    int maxWidgetWidth = 0;
	    int maxMarginLeft = 0;
	    int maxMarginRight = 0;

		for (int i = 0, len = getWidgetCount(); i < len; i++) {
			Widget widget = getWidget(i);

			BoxLayoutData layoutData = null;
			Object d = widget.getLayoutData();
			if (d instanceof BoxLayoutData) {
				layoutData = (BoxLayoutData) d;
			} else {
				layoutData = new BoxLayoutData();
				widget.setLayoutData(layoutData);
			}

			Margins cm = layoutData.getMargins();
			if (cm == null) {
				cm = new Margins(0);
				layoutData.setMargins(cm);
			}
		}

		for (int i = 0, len = getWidgetCount(); i < len; i++) {
			Widget widget = getWidget(i);

			if (!widget.isVisible()) {
				continue;
			}

			BoxLayoutData layoutData = (BoxLayoutData) widget
					.getLayoutData();
			Margins cm = layoutData.getMargins();

			calculateHeight += widget.getOffsetHeight();
			maxWidgetWidth = Math.max(maxWidgetWidth,
					widget.getOffsetWidth());

			calculateHeight += (cm.getTop() + cm.getBottom());
			maxMarginLeft = Math.max(maxMarginLeft, cm.getLeft());
			maxMarginRight = Math.max(maxMarginRight, cm.getRight());
		}
		maxWidgetWidth += (maxMarginLeft + maxMarginRight);

		h = calculateHeight;
		if (findWidth) {
			w = maxWidgetWidth;
		}

		int pl = 0;
		int pt = 0;
		int pb = 0;
		int pr = 0;
		if (getPadding() != null) {
			pl = getPadding().getLeft();
			pt = getPadding().getTop();
			pb = getPadding().getBottom();
			pr = getPadding().getRight();
		}

		h += pt + pb;
		if (findWidth) {
			w += pl + pr;
		}

		int stretchWidth = w - pl - pr;
		int totalFlex = 0;
		int totalHeight = 0;
		int maxWidth = 0;
		for (int i = 0, len = getWidgetCount(); i < len; i++) {
			Widget widget = getWidget(i);
			widget.addStyleName(CommonStyles.get().positionable());

			widget.getElement().getStyle().setMargin(0, Unit.PX);

			// callLayout(widget, false);

			BoxLayoutData layoutData = (BoxLayoutData) widget.getLayoutData();
			Margins cm = layoutData.getMargins();

			totalFlex += layoutData.getFlex();
			totalHeight += widget.getOffsetHeight() + cm.getTop()
					+ cm.getBottom();
			maxWidth = Math.max(maxWidth,
					widget.getOffsetWidth() + cm.getLeft() + cm.getRight());
		}

		int innerCtWidth = maxWidth + pl + pr;

		if (getVBoxLayoutAlign().equals(VBoxLayoutAlign.STRETCH)) {
			getContainerTarget().setSize(w, h, true);
		} else {
			getContainerTarget().setSize(w = Math.max(w, innerCtWidth), h, true);
		}

		int extraHeight = h - totalHeight - pt - pb;
		int allocated = 0;
		int cw, ch, cl;
		int availableWidth = w - pl - pr;

		if (getPack().equals(BoxLayoutPack.CENTER)) {
			pt += extraHeight / 2;
		} else if (getPack().equals(BoxLayoutPack.END)) {
			pt += extraHeight;
		}

		for (int i = 0, len = getWidgetCount(); i < len; i++) {
			Widget widget = getWidget(i);

			BoxLayoutData layoutData = (BoxLayoutData) widget.getLayoutData();
			Margins cm = layoutData.getMargins();

			cw = widget.getOffsetWidth();
			ch = widget.getOffsetHeight();
			pt += cm.getTop();
			if (getVBoxLayoutAlign().equals(VBoxLayoutAlign.CENTER)) {
				int diff = availableWidth - (cw + cm.getLeft() + cm.getRight());
				if (diff == 0) {
					cl = pl + cm.getLeft();
				} else {
					cl = pl + cm.getLeft() + (diff / 2);
				}
			} else {
				if (getVBoxLayoutAlign().equals(VBoxLayoutAlign.RIGHT)) {
					cl = w - (pr + cm.getRight() + cw);
				} else {
					cl = pl + cm.getLeft();
				}
			}

			boolean component = widget instanceof Component;
			Component c = null;
			if (component) {
				c = (Component) widget;
			}

			int height = -1;
			if (component) {
				c.setPosition(cl, pt);
			} else {
				XElement.as(widget.getElement()).setLeftTop(cl, pt);
			}

			if (getPack().equals(BoxLayoutPack.START)
					&& layoutData.getFlex() > 0) {
				int add = (int) Math.floor(extraHeight
						* (layoutData.getFlex() / totalFlex));
				allocated += add;
				if (isAdjustForFlexRemainder() && i == getWidgetCount() - 1) {
					add += extraHeight - allocated;
				}
				ch += add;
				height = ch;
			}
			if (getVBoxLayoutAlign().equals(VBoxLayoutAlign.STRETCH)) {
				applyLayout(widget, Util.constrain(stretchWidth - cm.getLeft()
						- cm.getRight(), layoutData.getMinSize(),
						layoutData.getMaxSize()), height);
			} else if (getVBoxLayoutAlign().equals(VBoxLayoutAlign.STRETCHMAX)) {
				applyLayout(widget, Util.constrain(
						maxWidth - cm.getLeft() - cm.getRight(),
						layoutData.getMinSize(), layoutData.getMaxSize()),
						height);
			} else if (height > 0) {
				applyLayout(widget, -1, height);
			}
			pt += ch + cm.getBottom();
		}

		if(autoHeight){
			getElement().getParentElement().getStyle().clearHeight(); //清理上级高度
			getElement().getStyle().clearHeight();
		}
	}
}
