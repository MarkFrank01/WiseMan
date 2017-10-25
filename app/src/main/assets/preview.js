/**
 * Copyright (C) 2017 Wasabeef
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var RE = {};

RE.title = document.getElementById('title');
RE.body = document.getElementById('body');

RE.setTitle = function(contents) {
    RE.title.innerHTML = decodeURIComponent(contents.replace(/\+/g, '%20'));
}

RE.setBody = function(contents) {
    RE.body.innerHTML = decodeURIComponent(contents.replace(/\+/g, '%20'));
}

RE.insertImage = function(url) {
    var html = '<img src="' + url + '" />';
    RE.insertHTML(html);
}

RE.insertHTML = function(html) {
    RE.restorerange();
    document.execCommand('insertHTML', false, html);
}