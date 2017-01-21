// sc is an existing SparkContext.
val sqlContext = new org.apache.spark.sql.SQLContext(sc)



//----------------------------------------------------------
import java.io.File
import java.util.Calendar
import java.text.SimpleDateFormat

//
// We define a new process instance 
// 
val now = Calendar.getInstance().getTime()
val timeFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
val PROCESS_ID = "mdloader-run_01_" + timeFormat.format(now).toString
println( PROCESS_ID )
val folder = new File("./../../../prolog/" + PROCESS_ID);
folder.mkdir();
//----------------------------------------------------------

//
// The REPO holds models from which MD has to be taken 
//
val REPO = folder.getParentFile + "/"
//----------------------------------------------------------


// A JSON dataset is pointed to by path.
// The path can be either a single text file or a directory storing text files.
// We assume, that all metadata files are in the same level of the file system tree.
//
// If this is not the case, then we need a separate PATH-Iterator in order to provide all
// elements we are looking for.
val path = "./../../../data/persistence_test/*/*/*/*/*/*/u*.json"
//----------------------------------------------------------


/*----------------------------------------------------------
In interation 1 we have the following schema:

root
 |-- accuracyMeasured: double (nullable = true)
 |-- accuracyRequired: double (nullable = true)
 |-- desc: string (nullable = true)
 |-- extra: struct (nullable = true)
 |    |-- This: string (nullable = true)
 |-- family: string (nullable = true)
 |-- id: string (nullable = true)
 |-- nrOfData: long (nullable = true)
 |-- stages: array (nullable = true)
 |    |-- element: string (containsNull = true)
 |-- version: string (nullable = true)

-------------------------------------------------------------*/

val mdrecords = sqlContext.read.json(path).toDF();

// Register this DataFrame as a table.
mdrecords.registerTempTable("mdr")

// The inferred schema can be visualized using the printSchema() method.
mdrecords.printSchema()
mdrecords.count()

// 
// A little bit of debugging here ...
//
val projected = mdrecords.select( $"id", $"accuracyMeasured", $"accuracyRequired" )
projected.collect().foreach(println);

// 
// What do we plan to do with our MD records?
//
import org.apache.spark.sql._
def ingestMDRecord (row:Row) : String = {
    
    var valid = false
    
    val a = row(0) 
    val b = row(1)

    if ( a == null || b == 0 ) {
       valid = false;
    }
    else { 
       val va = a.toString 
       val vb = b.toString 
 	   if ( va > vb ) {
 	     valid = true
 	   }
 	   else {
 	     valid = false 
 	   }
 	}

	val line = row(5) + " : [" + valid + "] <" + row(0) + "; " + row(1) + ">"
	
	//
	//
	//------------------------------------------------------
	if ( valid ) {

        // get valid ID to attach ... here we need the path of the file we look into ... 
        

        val doc = '{"name":"my_name","description":"My description", "tags":["tag1","tag2"],"properties":{"property1":"abc","property2":"def"}}' 

	}


	//
	//
	//------------------------------------------------------
	
    return ">>> " + line
}

//
// Process individual JSON records ...
//
val results = mdrecords.map( rec => ingestMDRecord( rec ) )
results.collect().foreach(println);






