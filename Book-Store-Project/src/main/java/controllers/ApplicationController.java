/**
 * Copyright (C) 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers;

import java.util.List;
import java.util.Map;

import models.Article;
import models.Book;
import ninja.Context;
import ninja.Result;
import ninja.Results;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

import dao.ArticleDao;
import dao.BookDao;
import dao.SetupDao;

public class ApplicationController {

    @Inject
    ArticleDao articleDao;
    @Inject
    BookDao bookDao;
    @Inject
    SetupDao setupDao;
    LoginLogoutController controller = new LoginLogoutController();

    public ApplicationController() {
    }

    /**
     * Method to put initial data in the db...
     * 
     * @return
     */
    
    public Result setup() {
        setupDao.setup();
        return Results.ok();
    }

    
    public Result index() {

        List<Book> frontPost = bookDao.getFirstBookForFrontPage();

        List<Book> olderPosts = bookDao.getOlderBooksForFrontPage();
        Long iid = controller.getcuser();
        if (iid!= 0L) {
        	frontPost = null;
        }
        Map<String, Object> map = Maps.newHashMap();
//        map.put("frontBook", frontPost);
        map.put("olderBooks", olderPosts);
        return Results.html().render("frontBook", frontPost)
                .render("olderBooks", olderPosts);

    }
}
