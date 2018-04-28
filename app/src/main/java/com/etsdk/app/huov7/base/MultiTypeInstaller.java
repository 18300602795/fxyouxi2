/*
 * Copyright 2016 drakeet. https://github.com/drakeet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.etsdk.app.huov7.base;

import com.etsdk.app.huov7.model.GameBean;
import com.etsdk.app.huov7.model.SplitLine;
import com.etsdk.app.huov7.provider.GameItemViewProvider;
import com.etsdk.app.huov7.provider.SplitLineViewProvider;

import me.drakeet.multitype.GlobalMultiTypePool;

/**
 * @author drakeet
 */
class MultiTypeInstaller {

    static void start() {
        GlobalMultiTypePool.register(GameBean.class, new GameItemViewProvider());
        GlobalMultiTypePool.register(SplitLine.class, new SplitLineViewProvider());
    }
}
