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

public class EmailsItem implements Comparable<EmailsItem> {
	@SerializedName("cc")
	private String cc;

	@SerializedName("date")
	private long date;

	@SerializedName("attachments")
	private List<Object> attachments;

	@SerializedName("dkim")
	private String dkim;

	@SerializedName("envelope_to")
	private String envelopeTo;

	@SerializedName("subject")
	private String subject;

	@SerializedName("SPF")
	private String sPF;

	@SerializedName("downloadUrl")
	private String downloadUrl;

	@SerializedName("messageId")
	private String messageId;

	@SerializedName("to_parsed")
	private List<ToParsedItem> toParsed;

	@SerializedName("oid")
	private String oid;

	@SerializedName("envelope_from")
	private String envelopeFrom;

	@SerializedName("cc_parsed")
	private List<Object> ccParsed;

	@SerializedName("namespace")
	private String namespace;

	@SerializedName("from")
	private String from;

	@SerializedName("sender_ip")
	private String senderIp;

	@SerializedName("html")
	private String html;

	@SerializedName("to")
	private String to;

	@SerializedName("tag")
	private String tag;

	@SerializedName("text")
	private String text;

	@SerializedName("id")
	private String id;

	@SerializedName("from_parsed")
	private List<FromParsedItem> fromParsed;

	@SerializedName("timestamp")
	private Long timestamp;

	public String getCc(){
		return cc;
	}

	public Long getDate(){
		return date;
	}

	public List<Object> getAttachments(){
		return attachments;
	}

	public String getDkim(){
		return dkim;
	}

	public String getEnvelopeTo(){
		return envelopeTo;
	}

	public String getSubject(){
		return subject;
	}

	public String getSPF(){
		return sPF;
	}

	public String getDownloadUrl(){
		return downloadUrl;
	}

	public String getMessageId(){
		return messageId;
	}

	public List<ToParsedItem> getToParsed(){
		return toParsed;
	}

	public String getOid(){
		return oid;
	}

	public String getEnvelopeFrom(){
		return envelopeFrom;
	}

	public List<Object> getCcParsed(){
		return ccParsed;
	}

	public String getNamespace(){
		return namespace;
	}

	public String getFrom(){
		return from;
	}

	public String getSenderIp(){
		return senderIp;
	}

	public String getHtml(){
		return html;
	}

	public String getTo(){
		return to;
	}

	public String getTag(){
		return tag;
	}

	public String getText(){
		return text;
	}

	public String getId(){
		return id;
	}

	public List<FromParsedItem> getFromParsed(){
		return fromParsed;
	}

	public long getTimestamp(){
		return timestamp;
	}

	@Override
	public int compareTo(EmailsItem o) {
		return getDate().compareTo(o.getDate());
	}
}