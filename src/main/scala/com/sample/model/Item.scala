package com.sample.model

import java.util.Date
import java.text.DateFormat

import net.liftweb.mapper.{LongKeyedMapper,
                           LongKeyedMetaMapper,
                           IdPK,
                           MappedDateTime,
                           MappedString,
                           By}

import scala.xml.Text

class Item extends LongKeyedMapper[Item] with IdPK {
    def getSingleton = Item

    object name extends MappedString(this, 100) {
        override def validations = valMinLen(5, "name must be at least 5 characters") _ ::
                                   valUnique("Name must be unique") _ :: super.validations
    }

    object date extends MappedDateTime(this) {
        override def defaultValue = new Date()
        val dateFormat  = DateFormat.getDateInstance(DateFormat.SHORT)
        override  def  asHtml  =  Text(dateFormat.format(this))
    }
}

object Item extends Item with LongKeyedMetaMapper[Item] {
    def findByName(name: String) : List[Item] = {
        Item.findAll(By(Item.name, name))
    }
}
