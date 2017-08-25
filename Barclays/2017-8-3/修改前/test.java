package com.aif.rpt.biz.aml.processing.client;

import java.util.ArrayList;
import java.util.List;

import com.allinfinance.yak.uface.core.client.config.BaseConfig;
import com.allinfinance.yak.uface.core.client.config.PageViewConfig;
import com.allinfinance.yak.uface.core.shared.annotations.reflection.ClassForNameAble;
import com.allinfinance.yak.uface.data.client.command.DatasetCommand;
import com.allinfinance.yak.uface.data.client.dataset.ViewDataset;
import com.allinfinance.yak.uface.ui.component.client.basic.UButton;
import com.allinfinance.yak.uface.ui.component.client.form.UDynamicForm;
import com.allinfinance.yak.uface.ui.component.client.form.fields.USectionItem;
import com.allinfinance.yak.uface.ui.component.client.form.fields.UDateItem;
import com.allinfinance.yak.uface.ui.component.client.form.fields.UMoneyItem;
import com.allinfinance.yak.uface.ui.component.client.form.fields.USelectItem;
import com.allinfinance.yak.uface.ui.component.client.form.fields.UTextAreaItem;
import com.allinfinance.yak.uface.ui.component.client.form.fields.UTextItem;
import com.allinfinance.yak.uface.ui.component.client.form.fields.UCheckboxItem;
import com.allinfinance.yak.uface.ui.component.client.form.fields.UPasswordItem;
import com.allinfinance.yak.uface.ui.component.client.grid.UTable;
import com.allinfinance.yak.uface.ui.component.client.basic.UWindow;
import com.allinfinance.yak.uface.ui.component.client.tabset.UTabset;
import com.allinfinance.yak.uface.ui.component.client.tree.UTreeGrid;
import com.allinfinance.yak.uface.ui.component.client.label.ULabel;
import com.allinfinance.yak.uface.ui.component.client.calendar.UCalendar;
import com.allinfinance.yak.uface.ui.component.client.upload.UBaseUploader;
import com.allinfinance.yak.uface.ui.component.client.view.CommonPageView;

@ClassForNameAble
public class test extends CommonPageView {

    protected UWindow w1;

    @Override
    protected void afterInitDatasets() {
        super.afterInitDatasets();
    }

    @Override
    protected PageViewConfig initConfig() {
        PageViewConfig config = super.initConfig();
        BaseConfig render = config.getRenderConfig();
        super.afterInitControls();

        //datasets init begin
        BaseConfig dataset = config.getDatasetsConfig();
        //datasets init end

        //commands init begin
        BaseConfig command = config.getCommandsConfig();
        //commands init end

        //renders init begin
        BaseConfig lay1 = sub_control_lay1();
        render.put("lay1", lay1);

        //renders init end
        return config;
    }

    private BaseConfig sub_control_lay2() {
        BaseConfig lay2 = new BaseConfig();
        lay2.setXmlTag("Layout");
        lay2.put("id", "lay2");
        lay2.put("fragmentId", "");
        lay2.put("type", "Grid");
        lay2.put("row", "");
        lay2.put("column", "");
        lay2.put("labelAlign", "left");
        lay2.put("componentAlignment", "Top_Center");
        lay2.put("margin", "false");
        lay2.put("spacing", "false");
        lay2.put("columnExpandRatio", "");
        lay2.put("width", "");
        lay2.put("height", "");
        lay2.put("enable", "true");
        lay2.put("readOnly", "false");
        lay2.put("visible", "true");
        return lay2;
    }

    private BaseConfig sub_control_w1() {
        BaseConfig w1 = new BaseConfig();
        w1.setXmlTag("Window");
        w1.put("id", "w1");
        w1.put("fragmentId", "");
        w1.put("type", "Sub");
        w1.put("title", "");
        w1.put("closeable", "true");
        w1.put("draggable", "false");
        w1.put("resizeable", "false");
        w1.put("shortcut", "");
        w1.put("modal", "false");
        w1.put("autoCenter", "true");
        w1.put("autoSize", "true");
        w1.put("positionX", "");
        w1.put("positionY", "");
        w1.put("border", "1");
        w1.put("width", "");
        w1.put("height", "");
        w1.put("enable", "true");
        w1.put("readOnly", "false");
        w1.put("visible", "true");
        List<BaseConfig> w1List = new ArrayList<BaseConfig>();
        w1.put("w1List",w1List);
        BaseConfig lay2 = sub_control_lay2();
        w1List.add(lay2);
        return w1;
    }

    private BaseConfig sub_control_lay1() {
        BaseConfig lay1 = new BaseConfig();
        lay1.setXmlTag("Layout");
        lay1.put("id", "lay1");
        lay1.put("fragmentId", "");
        lay1.put("type", "Grid");
        lay1.put("row", "");
        lay1.put("column", "");
        lay1.put("labelAlign", "left");
        lay1.put("componentAlignment", "Top_Left");
        lay1.put("margin", "false");
        lay1.put("spacing", "false");
        lay1.put("columnExpandRatio", "");
        lay1.put("width", "");
        lay1.put("height", "");
        lay1.put("enable", "true");
        lay1.put("readOnly", "false");
        lay1.put("visible", "true");
        List<BaseConfig> lay1List = new ArrayList<BaseConfig>();
        lay1.put("lay1List",lay1List);
        BaseConfig w1 = sub_control_w1();
        lay1List.add(w1);
        return lay1;
    }

    @Override
    protected void afterInitControls() {
        super.afterInitControls();
        w1 = (UWindow)this.getControls().get("w1");
        return;
    }
}