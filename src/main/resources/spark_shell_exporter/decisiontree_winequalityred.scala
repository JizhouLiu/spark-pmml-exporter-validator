import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.mllib.tree.model.DecisionTreeModel
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors

// Load and parse the data
val data = sc.textFile("../datasets/winequalityred.csv")
val parsedData = data.map { line =>
  val parts = line.split(';')
  LabeledPoint(parts.last.toDouble, Vectors.dense(parts.take(11).map(_.toDouble)))
}

// Train a DecisionTree model.
//  Empty categoricalFeaturesInfo indicates all features are continuous.
val categoricalFeaturesInfo = Map[Int, Int]()
val impurity = "variance"
val maxDepth = 5
val maxBins = 32

val model = DecisionTree.trainRegressor(parsedData, categoricalFeaturesInfo, impurity,
  maxDepth, maxBins)

// Export decision tree model to PMML
model.toPMML("../exported_pmml_models/decisiontree_regression.xml")

// Test model on training data
// First from winequalityred.csv (quality: 5)
var predictedValue = model.predict(Vectors.dense(7.4,0.7,0,1.9,0.076,11,34,0.9978,3.51,0.56,9.4))
// Random from winequalityred.csv (quality: 7)
predictedValue = model.predict(Vectors.dense(11.5,0.54,0.71,4.4,0.124,6,15,0.9984,3.01,0.83,11.8))
