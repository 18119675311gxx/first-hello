package com.beizeng.admin.common.sys.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 放屁宣言
 *
 * @author:
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LiRen {

    //  声明：本人不主动，不拒绝，不负责，以上代码均来自互联网。
    //  没事别瞎动标记这个注解的代码哦！
    //  容老夫 回来写 这个注解。不要着急哦。

}
