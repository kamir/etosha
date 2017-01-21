// sc is an existing SparkContext.
val sqlContext = new org.apache.spark.sql.SQLContext(sc)



//----------------------------------------------------------
import java.io.File
import java.util.Calendar
import java.text.SimpleDateFormat

// We define a new process instance 
// 
val now = Calendar.getInstance().getTime()

val timeFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")

val PROCESS_ID = "xware42-run4_" + timeFormat.format(now).toString

println( PROCESS_ID )
val folder = new File("./../../../repo/" + PROCESS_ID);
folder.mkdir();

val REPO = folder.getParentFile + "/"
//----------------------------------------------------------


// A JSON dataset is pointed to by path.
// The path can be either a single text file or a directory storing text files.
val path = "./../../../data/xware/run4/TEST3*.json"

val vftx4 = sqlContext.read.json(path).toDF();

// Register this DataFrame as a table.
vftx4.registerTempTable("vftx4")

// The inferred schema can be visualized using the printSchema() method.
vftx4.printSchema()

vftx4.count()

// SQL statements can be run by using the sql methods provided by sqlContext.
// val googleEnriched = sqlContext.sql("SELECT orig_tx_id FROM vftx4 WHERE age >= 13 AND age <= 19")
// val yelpEnriched = sqlContext.sql("SELECT orig_tx_id FROM people WHERE age >= 13 AND age <= 19")

