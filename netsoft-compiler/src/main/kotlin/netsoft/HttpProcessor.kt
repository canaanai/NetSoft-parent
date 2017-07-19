package netsoft

import com.google.auto.common.SuperficialValidation
import com.google.auto.service.AutoService
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.*
import javax.lang.model.type.TypeKind
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic

/**
 * @author chenp
 * @version 2017-07-11 11:49
 */
@AutoService(Processor::class)
class HttpProcessor : AbstractProcessor() {
    lateinit var mElementUtils: Elements
    lateinit var mTypeUtils: Types
    lateinit var mFiler: Filer
    lateinit var mMessages: Messager

    private val annoData = hashMapOf<TypeElement, ArrayList<ExecutableElement>>()

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(HttpGet::class.java.canonicalName, HttpPost::class.java.canonicalName)
    }

    override @Synchronized fun init(env: ProcessingEnvironment) {
        super.init(env)

        //Element代表程序的元素，例如包、类、方法。
        mElementUtils = env.elementUtils

        //处理TypeMirror的工具类，用于取类信息
        mTypeUtils = env.typeUtils

        //Filer可以创建文件
        mFiler = env.filer

        //错误处理工具
        mMessages = env.messager
    }

    override fun process(elements: MutableSet<out TypeElement>?, env: RoundEnvironment): Boolean {
        env.getElementsAnnotatedWith(HttpGet::class.java)
                .filter { validateElement(it) }
                .forEach {
                    try {
                        //类信息
                        val classElement = it.enclosingElement as TypeElement
                        /*//类的绝对路径
                        val classFullName = classElement.qualifiedName.toString()*/
                        /*//类名
                        val className = classElement.simpleName.toString()
                        //包名
                        val packageName = mElementUtils.getPackageOf(classElement).qualifiedName.toString()*/

                        val methodElement = it as ExecutableElement
                        /*val httpAnnotation = methodElement.getAnnotation(HttpGet::class.java)
                        val requestClass = httpAnnotation.requestClass
                        val requestCode = httpAnnotation.requestCode
                        val scheduler = httpAnnotation.scheduler

                        val responseClass = methodElement.parameters[0].javaClass*/

                        if (!annoData.containsKey(classElement)){
                            annoData[classElement] = arrayListOf<ExecutableElement>()
                        }

                        annoData[classElement]?.add(methodElement)
                    }catch (e: Exception){
                        mMessages.printMessage(Diagnostic.Kind.ERROR, e.message, it)
                    }
                }

        return false
    }

    private fun validateElement(element: Element): Boolean{
        if (SuperficialValidation.validateElement(element)){
            if (element.kind == ElementKind.METHOD){
                val methodElement = element as ExecutableElement
                val modifiers = methodElement.modifiers

                if (!modifiers.contains(Modifier.PUBLIC) || modifiers.contains(Modifier.ABSTRACT)){
                    mMessages.printMessage(Diagnostic.Kind.ERROR, "该注解的方法必须是声明为Public的非抽象方法", element)
                }else{
                    return true
                }
            }else{
                mMessages.printMessage(Diagnostic.Kind.ERROR, "该注解只能用于类方法上", element)
            }
        }

        return false
    }

    private fun createJavaFile(classElement: TypeElement, methodElements: ArrayList<ExecutableElement>){
        val typeSpec = TypeSpec.classBuilder(classElement.simpleName.toString() + "HttpClient")
                .addModifiers(Modifier.PUBLIC)
        val methods = ArrayList<MethodSpec>()

        for (methodElement in methodElements){

            if (methodElement.parameters.size == 0){
                mMessages.printMessage(Diagnostic.Kind.ERROR, "方法参数必须有且只有一个", methodElement)
                continue
            }

            val subscription = ClassName.get("rx", "Subscription")
            val annotation = methodElement.getAnnotation(HttpGet::class.java)
            val methodSpecBuilder = MethodSpec.methodBuilder("onResponse" + annotation.requestTag)
                    .addModifiers(Modifier.PRIVATE)
                    .returns(subscription)

            if (annotation.requestClass != NullClass::class){
                methodSpecBuilder.addParameter(annotation.requestClass.java, "request")
            }
        }
    }
}