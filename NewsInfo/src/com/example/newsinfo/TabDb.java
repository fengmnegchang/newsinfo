/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-10-17下午3:37:42
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
package com.example.newsinfo;

/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2016-10-17下午3:37:42
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
public class TabDb {
	public static String[] getTabsTxt(){  
        String[] tabs={"首页","健康养生","应用号"};  
        return tabs;  
    }  
    public static int[] getTabsImg(){  
        int[] ids={R.drawable.returnhome,R.drawable.tab_app_new,R.drawable.tab_app_new};  
        return ids;  
    }  
    public static int[] getTabsImgLight(){  
        int[] ids={R.drawable.returnhome_h,R.drawable.tab_app_new_h,R.drawable.tab_app_new_h};  
        return ids;  
    }  
//    public static Class[] getFragments(){  
//        Class[] clz={NewsFragment.class,NewsFragment.class,NewsFragment.class};  
//        return clz;  
//    }  
    public static Class[] getActivitys(){  
        Class[] clz={NewsActivity.class,SampleTabsDefault.class,AppWebActivity.class};  
        return clz;  
    } 
}
