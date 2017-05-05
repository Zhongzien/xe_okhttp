package com.okhttplib.help.inter;

import com.okhttplib.help.OKHttpCommand;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public interface NetWorkInter {

    void doRequestAsync(OKHttpCommand command);

    void doRequestSync(OKHttpCommand command);
}
