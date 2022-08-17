package edu.neu.etl

import edu.neu.etl.ETLSparkApp.ratingDS
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.types.{StructField, StructType}

case object ETLCommons
{
  /**  set nullable property of dataset[RatingLog] **/

  def setNullableStateOfColumn(df: Dataset[RatingLog], cn: String, nullable: Boolean) = {

    // get schema
    val schema = df.schema
    // modify [[StructField] with name `cn`
    val newSchema = StructType(schema.map {
      case StructField(c, t, _, m) if c.equals(cn) => StructField(c, t, nullable = nullable, m)
      case y: StructField => y
    })
  }
}
