package org.json4s


import util.control.Exception._
import org.specs.Specification

object ParserBugs extends Specification {
  import native.JsonParser
  import native.JsonMethods._

  "Unicode ffff is a valid char in string literal" in {
    parseOpt(""" {"x":"\uffff"} """).isDefined mustEqual true
  }

  "Does not hang when parsing 2.2250738585072012e-308" in {
    allCatch.opt(parse(""" [ 2.2250738585072012e-308 ] """)) mustEqual None
    allCatch.opt(parse(""" [ 22.250738585072012e-309 ] """)) mustEqual None
  }

  "Does not allow colon at start of array (1039)" in {
    parseOpt("""[:"foo", "bar"]""") mustEqual None
  }

  "Does not allow colon instead of comma in array (1039)" in {
    parseOpt("""["foo" : "bar"]""") mustEqual None
  }

  "Solo quote mark should fail cleanly (not StringIndexOutOfBoundsException) (1041)" in {
    JsonParser.parse("\"", discardParser) must throwA(new Exception()).like {
      case e: ParserUtil.ParseException => e.getMessage.startsWith("unexpected eof")
    }
  }

  "Field names must be quoted" in {
    val json = JObject(List(JField("foo\nbar", JInt(1))))
    val s = compact(render(json))
    s mustEqual """{"foo\nbar":1}"""
    parse(s) mustEqual json
  }

  private val discardParser = (p : JsonParser.Parser) => {
     var token: JsonParser.Token = null
     do {
       token = p.nextToken
     } while (token != JsonParser.End)
   }
}