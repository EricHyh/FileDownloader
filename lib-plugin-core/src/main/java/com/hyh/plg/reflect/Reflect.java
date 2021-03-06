package com.hyh.plg.reflect;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;

//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                      .   ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//               佛祖镇楼                  BUG辟易
//         佛曰:
//                  写字楼里写字间，写字间里程序员；
//                  程序人员写程序，又拿程序换酒钱。
//                  酒醒只在网上坐，酒醉还来网下眠；
//                  酒醉酒醒日复日，网上网下年复年。
//                  但愿老死电脑间，不愿鞠躬老板前；
//                  奔驰宝马贵者趣，公交自行程序员。
//                  别人笑我忒疯癫，我笑自己命太贱；
//                  不见满街漂亮妹，哪个归得程序员？
public class Reflect {

    private static final String TAG = "Reflect";

    private static final HashMap<String, Class> CLASS_MAP = new HashMap<>();

    private static final HashMap<String, Field> FIELD_MAP = new HashMap<>();

    private static final HashMap<String, Method> METHOD_MAP = new HashMap<>();

    private static final HashMap<String, Constructor> CONSTRUCTOR_MAP = new HashMap<>();

    //用于保证返回值为基础数据类型时，不返回null
    private static final HashMap<Class, Object> PRIMITIVE_DEFAULT_VALUE = new HashMap<>(8);

    static {
        PRIMITIVE_DEFAULT_VALUE.put(byte.class, Byte.valueOf("0"));
        PRIMITIVE_DEFAULT_VALUE.put(short.class, Short.valueOf("0"));
        PRIMITIVE_DEFAULT_VALUE.put(int.class, 0);
        PRIMITIVE_DEFAULT_VALUE.put(long.class, 0L);
        PRIMITIVE_DEFAULT_VALUE.put(float.class, 0.0f);
        PRIMITIVE_DEFAULT_VALUE.put(double.class, 0.0d);
        PRIMITIVE_DEFAULT_VALUE.put(boolean.class, false);
        PRIMITIVE_DEFAULT_VALUE.put(char.class, '\u0000');
    }

    public static boolean isAssignableFrom(Class<?> child, Class<?> parent) {
        if (child == parent) {
            return true;
        }
        if (!parent.isPrimitive()) {
            return parent.isAssignableFrom(child);
        }
        if (parent == byte.class) {
            return child == Byte.class;
        } else if (parent == short.class) {
            return child == Short.class;
        } else if (parent == int.class) {
            return child == Integer.class;
        } else if (parent == long.class) {
            return child == Long.class;
        } else if (parent == float.class) {
            return child == Float.class;
        } else if (parent == double.class) {
            return child == Double.class;
        } else if (parent == boolean.class) {
            return child == Boolean.class;
        } else if (parent == char.class) {
            return child == Character.class;
        } else if (parent == void.class) {
            return child == Void.class;
        }
        return false;
    }

    public static boolean isChildClassLoader(ClassLoader child, ClassLoader targetParent) {
        if (child == null) return false;
        ClassLoader parent = child.getParent();
        if (parent == null) {
            return false;
        }
        boolean result;
        do {
            result = parent == targetParent;
            if (result) break;
            parent = parent.getParent();
        } while (parent != null);
        return result;
    }

    public static Throwable getRealThrowable(Throwable throwable) {
        return getRealThrowable(throwable, 0);
    }

    private static Throwable getRealThrowable(Throwable throwable, int num) {
        if (throwable == null || num >= 16) {
            return throwable;
        }
        if (throwable instanceof InvocationTargetException || throwable instanceof UndeclaredThrowableException) {
            Throwable cause = throwable.getCause();
            if (cause == null) {
                return throwable;
            } else {
                return getRealThrowable(cause, ++num);
            }
        } else {
            return throwable;
        }
    }


    @SuppressWarnings("unchecked")
    public static <E> E getDefaultValue(Class<E> valueType) {
        return (E) PRIMITIVE_DEFAULT_VALUE.get(valueType);
    }


    @SuppressWarnings("unchecked")
    public static <T> T safeCast(Object object, Class<T> type) {
        if (object == null) {
            return Reflect.getDefaultValue(type);
        }
        Class<?> aClass = object.getClass();
        if (Reflect.isAssignableFrom(aClass, type)) {
            return (T) object;
        } else {
            return Reflect.getDefaultValue(type);
        }
    }

    public static Class classForName(String className) {
        String key = generateClassMapKey(Reflect.class.getClassLoader(), className);
        if (CLASS_MAP.containsKey(key)) {
            return CLASS_MAP.get(key);
        }
        Class result = null;
        try {
            result = Class.forName(className);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        CLASS_MAP.put(key, result);
        return result;
    }


    public static Class classForName(ClassLoader classLoader, String className) {
        String key = generateClassMapKey(classLoader, className);
        if (CLASS_MAP.containsKey(key)) {
            return CLASS_MAP.get(key);
        }
        Class result = null;
        try {
            result = classLoader.loadClass(className);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        CLASS_MAP.put(key, result);
        return result;
    }


    public static Class classForNameWithException(String className) throws ClassNotFoundException {
        String key = generateClassMapKey(Reflect.class.getClassLoader(), className);
        Class result = CLASS_MAP.get(key);
        if (result == null) {
            result = Class.forName(className);
            CLASS_MAP.put(key, result);
        }
        return result;
    }

    public static Class classForNameWithException(ClassLoader classLoader, String className) throws ClassNotFoundException {
        if (classLoader == null) {
            return classForNameWithException(className);
        }
        String key = generateClassMapKey(classLoader, className);
        Class result = CLASS_MAP.get(key);
        if (result == null) {
            result = classLoader.loadClass(className);
            CLASS_MAP.put(key, result);
        }
        return result;
    }


    public static Field getDeclaredField(Class cls, String fieldName) {
        String key = generateFieldMapKey(cls, fieldName);
        if (FIELD_MAP.containsKey(key)) {
            return FIELD_MAP.get(key);
        }
        Field field = null;
        Class target = cls;
        do {
            try {
                field = target.getDeclaredField(fieldName);
                field.setAccessible(true);
            } catch (Exception e) {
                //IGNORE
            }
            if (field != null) break;
            target = target.getSuperclass();
        } while (target != null);
        FIELD_MAP.put(key, field);
        return field;
    }

    public static Field getDeclaredFieldWithException(Class cls, String fieldName) throws Throwable {
        String key = generateFieldMapKey(cls, fieldName);
        Field field = FIELD_MAP.get(key);
        if (field != null) {
            return field;
        }

        Throwable throwable = null;
        Class target = cls;
        do {
            try {
                field = target.getDeclaredField(fieldName);
                field.setAccessible(true);
            } catch (Throwable e) {
                if (throwable == null) {
                    throwable = e;
                }
            }
            if (field != null) break;
            target = target.getSuperclass();
        } while (target != null);

        if (field == null) {
            throw throwable;
        }

        FIELD_MAP.put(key, field);
        return field;
    }


    public static Method getDeclaredMethod(Class cls, String methodName, Class... parameterTypes) {
        String key = generateMethodMapKey(cls, methodName, parameterTypes);
        if (METHOD_MAP.containsKey(key)) {
            return METHOD_MAP.get(key);
        }
        Method method = null;
        Class target = cls;
        do {
            try {
                method = target.getDeclaredMethod(methodName, parameterTypes);
                method.setAccessible(true);
            } catch (Exception e) {
                //IGNORE
            }
            if (method != null) break;
            target = target.getSuperclass();
        } while (target != null);
        METHOD_MAP.put(key, method);
        return method;
    }


    public static Method getDeclaredMethodWithException(Class cls, String methodName, Class... parameterTypes) throws Throwable {
        String key = generateMethodMapKey(cls, methodName, parameterTypes);
        Method method = METHOD_MAP.get(key);
        if (method != null) {
            return method;
        }
        Throwable throwable = null;
        Class target = cls;
        do {
            try {
                method = target.getDeclaredMethod(methodName, parameterTypes);
                method.setAccessible(true);
            } catch (Throwable e) {
                if (throwable == null) {
                    throwable = e;
                }
            }
            if (method != null) break;
            target = target.getSuperclass();
        } while (target != null);

        if (method == null) {
            throw throwable;
        }

        METHOD_MAP.put(key, method);
        return method;
    }


    public static <T> Constructor<T> getDeclaredConstructor(Class<T> cls, Class[] parameterTypes) {
        String key = generateConstructorMapKey(cls, parameterTypes);
        if (CONSTRUCTOR_MAP.containsKey(key)) return CONSTRUCTOR_MAP.get(key);
        Constructor constructor = null;
        try {
            constructor = cls.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
        } catch (Exception e) {
            //IGNORE
        }
        CONSTRUCTOR_MAP.put(key, constructor);
        return constructor;
    }

    public static <T> Constructor<T> getDeclaredConstructorWithException(Class<T> cls, Class[] parameterTypes) throws Throwable {
        String key = generateConstructorMapKey(cls, parameterTypes);
        Constructor constructor = CONSTRUCTOR_MAP.get(key);
        if (constructor != null) {
            return constructor;
        }
        constructor = cls.getDeclaredConstructor(parameterTypes);
        constructor.setAccessible(true);
        CONSTRUCTOR_MAP.put(key, constructor);
        return constructor;
    }

    private static String generateClassMapKey(ClassLoader classLoader, String className) {
        String key = "";
        int hashCode = System.identityHashCode(classLoader);
        key += hashCode;
        key += "-";
        key += className;
        return key;
    }

    private static String generateFieldMapKey(Class cls, String fieldName) {
        return System.identityHashCode(cls) + "-" + fieldName;
    }

    public static String generateMethodMapKey(Class cls, String methodName, Class[] parameterTypes) {
        StringBuilder key = new StringBuilder();
        key.append(System.identityHashCode(cls))
                .append("-")
                .append(methodName)
                .append("-");
        if (parameterTypes == null || parameterTypes.length <= 0) {
            key.append("void");
        } else {
            int length = parameterTypes.length;
            for (int index = 0; index < length; index++) {
                if (index < length - 1) {
                    key.append(System.identityHashCode(parameterTypes[index])).append("-");
                } else {
                    key.append(System.identityHashCode(parameterTypes[index]));
                }
            }
        }
        return key.toString();
    }

    private static String generateConstructorMapKey(Class cls, Class[] parameterTypes) {
        StringBuilder key = new StringBuilder();
        key.append(System.identityHashCode(cls))
                .append("-");
        if (parameterTypes == null || parameterTypes.length <= 0) {
            key.append("void");
        } else {
            int length = parameterTypes.length;
            for (int index = 0; index < length; index++) {
                if (index < length - 1) {
                    key.append(System.identityHashCode(parameterTypes[index])).append("-");
                } else {
                    key.append(System.identityHashCode(parameterTypes[index]));
                }
            }
        }
        return key.toString();
    }


    public static <E> RefClassWG<E> from(Class<E> cls) {
        return new RefClassWG<>(cls);
    }

    public static RefClass from(String className) {
        return new RefClass(className);
    }

    public static RefClass from(ClassLoader classLoader, String className) {
        return new RefClass(classLoader, className);
    }

    public static boolean copyField(Object src, Object dest, String fieldName) {
        if (src == null || dest == null || TextUtils.isEmpty(fieldName)) return false;
        RefResult<Object> result = new RefResult<>();
        Reflect.from(src.getClass()).filed(fieldName).saveResult(result).get(src);
        if (!result.isSuccess()) return false;
        try {
            Reflect.from(dest.getClass()).filed(fieldName).setWithException(dest, result.getResult());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static Object sVmRuntime;
    private static Method setHiddenApiExemptions;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                Method forName = Class.class.getDeclaredMethod("forName", String.class);
                Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);

                Class<?> vmRuntimeClass = (Class<?>) forName.invoke(null, "dalvik.system.VMRuntime");
                Method getRuntime = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null);
                setHiddenApiExemptions = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions", new Class[]{String[].class});
                sVmRuntime = getRuntime.invoke(null);
            } catch (Throwable e) {
                Log.e(TAG, "reflect bootstrap failed:", e);
            }
        }
    }

    public static boolean exemptAll() {
        return exempt("L");
    }

    public static boolean exempt(String... methods) {
        if (sVmRuntime == null || setHiddenApiExemptions == null) {
            return false;
        }
        try {
            setHiddenApiExemptions.invoke(sVmRuntime, new Object[]{methods});
            return true;
        } catch (Throwable e) {
            return false;
        }
    }
}