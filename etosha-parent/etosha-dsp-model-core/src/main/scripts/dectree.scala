import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.mllib.tree.model.DecisionTreeModel
import org.apache.spark.mllib.util.MLUtils


//----------------------------------------------------------
import java.io.File
import java.util.Calendar
import java.text.SimpleDateFormat

// We define a new process instance 
// 
val now = Calendar.getInstance().getTime()

val timeFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")

val PROCESS_ID = "dectree_" + timeFormat.format(now).toString


println( PROCESS_ID )
val folder = new File("./../../../repo/" + PROCESS_ID);
folder.mkdir();

val REPO = folder.getParentFile + "/"

//----------------------------------------------------------


// Load and parse the data file.
val data = MLUtils.loadLibSVMFile(sc, "./../../../data/mllib/sample_libsvm_data.txt")
// Split the data into training and test sets (30% held out for testing)
val splits = data.randomSplit(Array(0.7, 0.3))
val (trainingData, testData) = (splits(0), splits(1))

// Train a DecisionTree model.
//  Empty categoricalFeaturesInfo indicates all features are continuous.
val categoricalFeaturesInfo = Map[Int, Int]()
val impurity = "variance"
val maxDepth = 5
val maxBins = 32

val model = DecisionTree.trainRegressor(trainingData, categoricalFeaturesInfo, impurity,
  maxDepth, maxBins)

// Evaluate model on test instances and compute test error
val labelsAndPredictions = testData.map { point =>
  val prediction = model.predict(point.features)
  (point.label, prediction)
}

val testMSE = labelsAndPredictions.map{ case (v, p) => math.pow(v - p, 2) }.mean()
println("Test Mean Squared Error = " + testMSE)
println("Learned regression tree model:\n" + model.toDebugString)

// Save and load model
model.save(sc, REPO + PROCESS_ID)
val sameModel = DecisionTreeModel.load(sc, REPO + PROCESS_ID)