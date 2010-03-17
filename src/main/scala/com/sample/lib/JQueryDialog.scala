package com.sample.lib

import _root_.scala.xml.{Group, NodeSeq}
import _root_.net.liftweb.http.js.{JsCmd}
import _root_.net.liftweb.http.{S}
import _root_.net.liftweb.http.js.{JE, JsCmd, JsCmds}
import _root_.net.liftweb.util.{AltXML, Helpers}

import Helpers._
import JE._
import JsCmds._

object JQueryDialog {
    def apply(html: NodeSeq) = new JQueryDialog(html, "")
    def apply(html: NodeSeq, title: String) = new JQueryDialog(html, title)
    def apply(html: NodeSeq, title: String, isModal: Boolean) = {
        new JQueryDialog(html, title) {
            override def options = "modal:%s".format(isModal) :: super.options
        }
    }
    def apply(html: NodeSeq, title: String, isModal: Boolean, id: String) = {
        new JQueryDialog(html, title) {
            override def elementId  = id
            override def options = "modal:%s".format(isModal) :: super.options
        }
    }

    def close(id: String): JsCmd = {
        JE.JsRaw("jQuery('#"+id+"').remove();")
    }
}

class JQueryDialog(html: NodeSeq, title: String) extends JsCmd {
    def _formatString(value: String) = value.encJs

    def _formatHtml(node: NodeSeq) =
        AltXML.toXML(Group(S.session.map(s =>
            s.fixHtml(s.processSurroundAndInclude("Modal Dialog", html))).openOr(html)), false, true, S.ieMode).encJs

    def _formatOptions: String =
        "{%s}".format(this.options.tail.foldLeft(this.options.head)((a, b) => a + "," + b))

    // Default options
    protected val cssClass: String = "dialog_box"
    protected val minWidth: String = "400"
    protected val width: String = "500"

    protected def elementId: String = "dialog_box"
    protected def options: List[String] = "title:%s".format(title.encJs) ::
                                          "minWidth:%s".format(minWidth) ::
                                          "width:%s".format(width) :: Nil

    lazy val js = "jQuery(" + _formatString("<div id='%s' class='%s'></div>".format(elementId, cssClass)) + ")" +
        ".html(" + _formatHtml(html) + ")" +
        ".dialog(" + _formatOptions + ").dialog(" + _formatString("'open'") + ");"

    lazy val toJsCmd = js
}

