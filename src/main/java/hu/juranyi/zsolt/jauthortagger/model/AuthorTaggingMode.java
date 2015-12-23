/*
 * Copyright 2015 Zsolt Jurányi
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

package hu.juranyi.zsolt.jauthortagger.model;

/**
 * Every <code>JavaFile</code> has a <code>taggingMode</code> attribute which
 * tells the tagger how to handle the existing author list. There are 3 options:
 * SKIP, MERGE, or OVERWRITE.
 *
 * @author Zsolt Jurányi
 * @see #MERGE
 * @see #OVERWRITE
 * @see #SKIP
 * @see JavaFile
 *
 */
public enum AuthorTaggingMode {

	/**
	 * Indicates that the existing author list will not be deleted and only
	 * addition of new authors will be performed.
	 */
	MERGE, //
	/**
	 * Indicates that the existing author list will be erased and replaced with
	 * the new authors.
	 */
	OVERWRITE, //
	/**
	 * Indicates that the file will be skipped.
	 */
	SKIP;

}
