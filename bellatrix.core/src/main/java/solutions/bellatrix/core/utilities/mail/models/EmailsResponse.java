/*
 * Copyright 2022 Automate The Planet Ltd.
 * Author: Anton Angelov
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package solutions.bellatrix.core.utilities.mail.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmailsResponse{

	@SerializedName("result")
	private String result;

	@SerializedName("emails")
	private List<EmailsItem> emails;

	@SerializedName("offset")
	private int offset;

	@SerializedName("count")
	private int count;

	@SerializedName("limit")
	private int limit;

	@SerializedName("message")
	private Object message;

	public String getResult(){
		return result;
	}

	public List<EmailsItem> getEmails(){
		return emails;
	}

	public int getOffset(){
		return offset;
	}

	public int getCount(){
		return count;
	}

	public int getLimit(){
		return limit;
	}

	public Object getMessage(){
		return message;
	}
}