package demo.annotation;

import lombok.experimental.FieldNameConstants;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@ClassAnnotation(info = "这是一个类注解")
public class TestClass {

    @FieldAnnotation(info = "这是一个成员变量注解")
    public int data;

    @MethodAnnotation(num = 25)
    private void testMethod() {
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Class clazz = Class.forName("demo.annotation.TestClass");
        System.out.println("---------类注解-----------");
        //如果该类有目标注解(ClassAnnotation.class)，则查看注解具体信息
        if (clazz.isAnnotationPresent(ClassAnnotation.class)) {
            System.out.println("类名：" + clazz.getName());
            ClassAnnotation classAnnotation = (ClassAnnotation) clazz.getAnnotation(ClassAnnotation.class);
            System.out.println(classAnnotation.info());
        }

        System.out.println("---------成员变量注解-----------");
        //clazz.getDeclaredFields() 获取对象所表示的类、接口中所 指定 声明的字段(包含public之外的三个修饰符),不包含继承的
        //clazz.getFields()         获取对象所表示的类、接口中所 指定 的 public字段(包含继承得到的,不包含其余三个修饰符)
        Field[] fields = clazz.getFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(FieldAnnotation.class)) ;
            {
                System.out.println("成员变量名：" + field.getName());
                FieldAnnotation fieldAnnotation = field.getAnnotation(FieldAnnotation.class);
                System.out.println(fieldAnnotation.info());
            }
        }

        System.out.println("---------成员方法注解-----------");
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            boolean hasAnnotation = method.isAnnotationPresent(MethodAnnotation.class);
            if (hasAnnotation) {
                System.out.println("方法名：" + method.getName());
                MethodAnnotation methodAnnotation = method.getAnnotation(MethodAnnotation.class);
                System.out.println("info:" + methodAnnotation.info());
            }

        }
    }
}
