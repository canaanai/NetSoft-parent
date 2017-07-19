package netsoft

import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

/**
 * @author chenp
 * @version 2017-07-14 15:04
 */
data class HttpAnnotationInfo(val classElement: TypeElement, val method: ExecutableElement)