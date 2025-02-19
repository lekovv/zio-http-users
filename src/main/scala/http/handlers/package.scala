package http

import exception.Exceptions.StatusNotFound
import zio.http.Response

package object handlers {
  val exceptionHandler: Throwable => Response = {
    case err: StatusNotFound => Response.internalServerError(s"Exception: $err")
    case err                 => Response.badRequest(s"Exception: $err")
  }
}
