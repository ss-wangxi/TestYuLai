package cc.snser.test.yulai.util;


import java.lang.reflect.Field;

import android.content.Context;

public class RUtils {
    private static String getPackageName(Context context) {
        //一个package直接这样返回，作为插件加载时，需要返回一个常量
        return context.getPackageName();
    }
    
	public static int getIdByName(Context context, String className, String name) {
		return context.getResources().getIdentifier(name, className, getPackageName(context));
	}
	
	@SuppressWarnings("rawtypes")
	private static int[] doGetIdsByname(Context context, String className, String name,boolean bNeedFind) {
		String packageName = getPackageName(context);
		Class r = null;
		try {
			if(bNeedFind){
				r = Class.forName(packageName + ".R");

				Class[] classes = r.getClasses();
				Class desireClass = null;

				for (int i = 0; i < classes.length; ++i) {
					if (classes[i].getName().split("\\$")[1].equals(className)) {
						desireClass = classes[i];
						break;
					}
				}

				if (desireClass != null){
					Field field = desireClass.getField(name);
					if(field != null){
						int[] array = (int[])field.get(null);
						return array;	
					}
				}
			}
			else {
				r = Class.forName(packageName + ".R$" + className);

				Field field = r.getField(name);
				if(field != null){
					int[] array = (int[])field.get(null);
					return array;	
				}
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static int[] getIdsByName(Context context, String className, String name) {

		int [] array = doGetIdsByname(context, className, name, false);
		if(array == null){
			array = doGetIdsByname(context, className, name, true);
		}
		
		return array;
	}
	
	public static final class id{
		public static int name(Context context, String name) {
			return RUtils.getIdByName(context, "id", name);
		}
	}
	public static final class drawable{
		public static int name(Context context, String name) {
			return RUtils.getIdByName(context, "drawable", name);
		}
	}
	public static final class layout{
		public static int name(Context context, String name) {
			return RUtils.getIdByName(context, "layout", name);
		}
	}
	public static final class string{
		public static int name(Context context, String name) {
			return RUtils.getIdByName(context, "string", name);
		}
	}
	public static final class style{
		public static int name(Context context, String name) {
			return RUtils.getIdByName(context, "style", name);
		}
	}
	public static final class anim{
		public static int name(Context context, String name) {
			return RUtils.getIdByName(context, "anim", name);
		}
	}
	public static final class color{
		public static int name(Context context, String name) {
			return RUtils.getIdByName(context, "color", name);
		}
	}
	public static final class attr{
		public static int name(Context context, String name) {
			return RUtils.getIdByName(context, "attr", name);
		}
	}
	public static final class dimen{
		public static int name(Context context, String name) {
			return RUtils.getIdByName(context, "dimen", name);
		}
	}
	public static final class styleable{
		public static int name(Context context, String name) {
			return RUtils.getIdByName(context, "styleable", name);
		}
		
		public static int[] idsname(Context context, String name){
			return RUtils.getIdsByName(context, "styleable", name);
		}
	}
	
	
}
