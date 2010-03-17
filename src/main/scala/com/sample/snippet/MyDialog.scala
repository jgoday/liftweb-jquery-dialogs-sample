package com.sample.snippet

import _root_.scala.xml.{NodeSeq}
import _root_.net.liftweb.http.{SHtml, TemplateFinder}
import _root_.net.liftweb.util.{Helpers}
import Helpers._

import com.sample.lib.{FormDialog}
import com.sample.model.{Item}

class MyDialog {
    def dialog(node: NodeSeq): NodeSeq = {
        FormDialog("templates-hidden/template", true).button("show dialog")
    }

    def edit(node: NodeSeq): NodeSeq = {
        val item = new Item
        def _template: NodeSeq = bind("item",
                    TemplateFinder.findAnyTemplate("templates-hidden/item" :: Nil) openOr
                        <div>{"Cannot find template"}</div>,
                    "name" -> item.name.toForm)

        val dialog = new FormDialog(true) {
            override def getFormContent = _template
            override def confirmDialog: NodeSeq = SHtml.ajaxSubmit("save",
                    () => {println(item);this.closeCmd}) ++ super.confirmDialog
        }

        dialog.button("edit item")
    }
}

