import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors


//----------------------------------------------------------
import java.io.File
import java.util.Calendar
import java.text.SimpleDateFormat

// We define a new process instance 
// 
val now = Calendar.getInstance().getTime()

val timeFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")

val PROCESS_ID = "kmeans_" + timeFormat.format(now).toString


println( PROCESS_ID )
val folder = new File("./../../../repo/" + PROCESS_ID);
folder.mkdir();

val REPO = folder.getParentFile + "/"

//----------------------------------------------------------



// Load and parse the data
val data = sc.textFile("./../../../data/mllib/kmeans_data.txt")
val parsedData = data.map(s => Vectors.dense(s.split(' ').map(_.toDouble))).cache()



// Cluster the data into two classes using KMeans
val numClusters = 2
val numIterations = 20
val clusters = KMeans.train(parsedData, numClusters, numIterations)



// Export to PMML to a String in PMML format
println("PMML Model:\n" + clusters.toPMML)

// Export the model to a local file in PMML format
clusters.toPMML( REPO + PROCESS_ID + "/kmeans.xml")

// Export the model to a directory on a distributed file system in PMML format
clusters.toPMML(sc, REPO + PROCESS_ID + "/kmeans")

