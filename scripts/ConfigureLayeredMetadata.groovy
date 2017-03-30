// Configure layered metadata:

//This is a demo LMD definition to be used until the user-configurable set up is available

import com.reallysi.rsuite.admin.importer.*
import com.reallysi.rsuite.client.api.*
import com.reallysi.rsuite.remote.api.*

rsuite.login();
lmDefs = rsuite.getLayeredMetaDataDefinitionInfos();
fields = [:];

def reportMetaDataMap(map) {
  def fieldName = map["name"];
  println "Metadata field: \"" + fieldName + "\""
  map.entrySet().each {
    def key = it.getKey();
    if (key != "name") {
      println "  " + key + "=" + it.getValue();
    }
  }
}

lmDefs.getMapList().each {

  def map = it.convertToMap() 
  def fieldName = map["name"];
  fields[map["name"]] = map["type"];
  reportMetaDataMap(map);
}

def addOrReplaceLMDDefinition(lmdName, associatedElements, allowedValues) {
  println " + [INFO] Metadata field \"" + lmdName + ":";
  if (fields.containsKey(lmdName)) {
    println " + [INFO]   Field exists, removing existing definition...";
    rsuite.removeLayeredMetaDataDefinition(lmdName);
  }
  println " + [INFO]   Creating new definition for \"" + lmdName + "\"...";
  println " + [INFO]     associated elements: " + associatedElements;
  println " + [INFO]     allowed values: " + allowedValues;
  def lmd = new LayeredMetaDataDefinition(lmdName, "string", associatedElements, allowedValues);
  rsuite.addLayeredMetaDataDefinition(lmd);
}

def addOrReplaceLMDDefinition(lmdName, associatedElements, allowedValues, versioned, allowsMultiple, allowContextual) {
  println " + [INFO] Metadata field \"" + lmdName + ":";
  if (fields.containsKey(lmdName)) {
    println " + [INFO]   Field exists, removing existing definition...";
    rsuite.removeLayeredMetaDataDefinition(lmdName);
  }
  println " + [INFO]   Creating new definition for \"" + lmdName + "\"...";
  println " + [INFO]     associated elements: " + associatedElements;
  println " + [INFO]     allowed values: " + allowedValues;
  println " + [INFO]     versioned: " + versioned;
  println " + [INFO]     allowsMultiple: " + allowsMultiple;
  println " + [INFO]     allowContextual: " + allowContextual;
  def lmd = new LayeredMetaDataDefinition(lmdName, "string", versioned, allowsMultiple, allowContextual, associatedElements, allowedValues);
  rsuite.addLayeredMetaDataDefinition(lmd);
}

def addOrReplaceLMDDefinitionDataType(lmdName, associatedElements, dataType) {
  println " + [INFO] [datatype] Metadata field \"" + lmdName + ":";
  if (fields.containsKey(lmdName)) {
    println " + [INFO]   Field exists, removing existing definition...";
    rsuite.removeLayeredMetaDataDefinition(lmdName);
  }
  println " + [INFO]   Creating new definition for \"" + lmdName + "\"...";
  println " + [INFO]     associated elements: " + associatedElements;
  println " + [INFO]     using data type: " + dataType["name"];
  def values = [];
  dataType.optionItemList.each {
    values.add(it["value"]);
  }
  println "+ [INFO]      values: =${values}";
  
  def lmd = new LayeredMetaDataDefinition(lmdName, "string", associatedElements, values);
  rsuite.addLayeredMetaDataDefinition(lmd);
}

println "Updating definitions"

                          
def nonXmlMoTypes = ['nonxml']
def assemblyTypes = ['rs_ca', 'rs_canode']
def moRootElemTypes = [
  'article', 
  'chapter',
  'subsection',
  'sidebar',
  'map', 
  'pubmap',
  'bookmap',
  'topic',
  '',
 ]

 
// Define data types used by LMD:
// def datatypeYesNo = new DataTypeDefinition("yesno", "string", "yes or no", "select", "literal");
// datatypeYesNo.addItem("yes", "yes");
// datatypeYesNo.addItem("no", "no");
// rsuite.setDataTypeDefinition(datatypeYesNo);

/**
 * Define LMD fields:
 */


//addOrReplaceLMDDefinition(lmdName, associatedElements, allowedValues, versioned, allowsMultiple, allowContextual)
addOrReplaceLMDDefinition("Tag", assemblyTypes + nonXmlMoTypes + moRootElemTypes, null, false, true, false);

