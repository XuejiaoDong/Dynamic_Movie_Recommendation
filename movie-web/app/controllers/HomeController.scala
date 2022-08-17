
package controllers

import edu.neu.recommendmovies.RecommendMovies
import producer.MessageSender
import javax.inject.Inject
//import scala.collection._
import play.api.mvc._
import play.api.libs.json._
import play.api.data.validation.Constraints._

//import play.api.i18n.Messages._


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

case class BasicForm(userid: Int, recommendNum: Int, genreNum:Int)
case class Result(res: String)
case class MovieRatingForm(userId: String, movieId: String, rating: String)
// this could be defined somewhere else,
// but I prefer to keep it in the companion object
object BasicForm {
  import play.api.data.Forms._
  import play.api.data.Form
  val form: Form[BasicForm] = Form(
    mapping(
      "userid" -> number,
      "recommendNum" -> number(min =1, max = 20),
      "genreNum" -> number(min =1, max = 20)
    )(BasicForm.apply)(BasicForm.unapply)
  )
}

object MovieRatingForm {
  import play.api.data.Forms._
  import play.api.data.Form
  val form: Form[MovieRatingForm] = Form(
    mapping(
      "userId" -> text,
      "movieId" -> text,
      "rating" -> text
    )(MovieRatingForm.apply)(MovieRatingForm.unapply)
  )
}

class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {
  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */



  def analysisPost() = Action { implicit request =>
    val formData: BasicForm = BasicForm.form.bindFromRequest.get // Careful: BasicForm.form.bindFromRequest returns an Option
    val useridtemp = formData.userid
    val movieNum = formData.recommendNum
    val genrenum = formData.genreNum
    //call the function to return movies here
    val result1 = RecommendMovies.youMightLike(useridtemp,movieNum)
    val result2 = RecommendMovies.topNMoviesPerGenre("crime",genrenum)
    val result3 = RecommendMovies.topNMoviesPerGenre("Action",genrenum)
    val result4 = RecommendMovies.topNMoviesPerGenre("Comedy",genrenum)
    val result5 = RecommendMovies.topNMoviesPerGenre("Horror",genrenum)
    //val finalresult = result1 + "\n" + result2
    Ok(views.html.personal(result1, result2,result3,result4,result5))
  }

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index(BasicForm.form))
  }

  def rating(movieId:String) = Action { implicit request:Request[AnyContent] =>
    Ok(views.html.rating(MovieRatingForm.form,movieId))
  }

  def submit()= Action { implicit request: Request[AnyContent] =>
    val ratingform: MovieRatingForm = MovieRatingForm.form.bindFromRequest.get
    val timestamp: Long = System.currentTimeMillis / 1000
    val result = (ratingform.userId, ratingform.movieId, ratingform.rating, timestamp.toString())

    //send rating data in tuples form to Kafka
    MessageSender.sendRatingToKafka(result)
    //bring user back to homepage
    Ok(views.html.index(BasicForm.form))
  }

  def addPoster(id: Int): String = {
    val result = "(" + "images/posters/"+ id + ".jpg" + ")"
    result
  }
}