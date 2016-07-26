package com.feng.web.model;

public class WxMessage {

	public static enum MessageType {
		TEXT, // �ı���Ϣ
		IMAGE, // ͼƬ��Ϣ
		VOICE, // ������Ϣ
		VIDEO, // ��Ƶ��Ϣ
		SHORTVIDEO, // С��Ƶ��Ϣ
		LOCATION, // ����λ����Ϣ
		LINK, // ������Ϣ
		EVENT// �¼���Ϣ
	}

	private String ToUserName;
	private String FromUserName;
	private String CreateTime;
	private String MsgType;// text image voice video shortvideo location link event
	private String MsgId;// ��Ϣid��64λ����

	// text
	private String Content;
	
//	// image voice video:
//	private String MediaId;// ý��id�����Ե��ö�ý���ļ����ؽӿ���ȡ���ݡ�
//	// image�����ֶ�
//	private String PicUrl;// ͼƬ����
//	// voice�����ֶ�
//	private String Format;// ������ʽ����amr��speex��
//	// video shortvideo�����ֶ�
//	private String ThumbMediaId;// ��Ƶ��Ϣ����ͼ��ý��id�����Ե��ö�ý���ļ����ؽӿ���ȡ���ݡ�
	private Image Image;
	private Voice Voice;
	private Video Video;
	private Music Music;
	
	// location �����ֶ�
	private String Location_X;// ����λ��ά��
	private String Location_Y;// ����λ�þ���
	private String Scale;// ��ͼ���Ŵ�С
	private String Label;// ����λ����Ϣ
	// link �����ֶ�
	private String Title;// ��Ϣ����
	private String Description;// ��Ϣ����
	private String Url;// ��Ϣ����
	//event �����ֶ�
	private String Event;
	private String EventKey;
	private String Ticket;

	public String getEvent() {
		return Event;
	}

	public void setEvent(String event) {
		Event = event;
	}

	public String getEventKey() {
		return EventKey;
	}

	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}

	public String getTicket() {
		return Ticket;
	}

	public void setTicket(String ticket) {
		Ticket = ticket;
	}

	public Image getImage() {
		return Image;
	}

	public void setImage(Image image) {
		Image = image;
	}

	public Voice getVoice() {
		return Voice;
	}

	public void setVoice(Voice voice) {
		Voice = voice;
	}

	public Video getVideo() {
		return Video;
	}

	public void setVideo(Video video) {
		Video = video;
	}

	public Music getMusic() {
		return Music;
	}

	public void setMusic(Music music) {
		Music = music;
	}

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getMsgId() {
		return MsgId;
	}

	public void setMsgId(String msgId) {
		MsgId = msgId;
	}

	public String getLocation_X() {
		return Location_X;
	}

	public void setLocation_X(String location_X) {
		Location_X = location_X;
	}

	public String getLocation_Y() {
		return Location_Y;
	}

	public void setLocation_Y(String location_Y) {
		Location_Y = location_Y;
	}

	public String getScale() {
		return Scale;
	}

	public void setScale(String scale) {
		Scale = scale;
	}

	public String getLabel() {
		return Label;
	}

	public void setLabel(String label) {
		Label = label;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public static class Image{
		String mediaId;
		public String getMediaId() {
			return mediaId;
		}
		public void setMediaId(String mediaId) {
			this.mediaId = mediaId;
		}
	}
	public static class Voice{
		String mediaId;
		public String getMediaId() {
			return mediaId;
		}
		public void setMediaId(String mediaId) {
			this.mediaId = mediaId;
		}
	}
	public static class Video{
		String mediaId;
		String title;
		String description;
		public String getMediaId() {
			return mediaId;
		}
		public void setMediaId(String mediaId) {
			this.mediaId = mediaId;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
	}
	public static class Music{
		String title;
		String description;
		String MusicUrl;
		String HQMusicUrl;
		String ThumbMediaId;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getMusicUrl() {
			return MusicUrl;
		}
		public void setMusicUrl(String musicUrl) {
			MusicUrl = musicUrl;
		}
		public String getHQMusicUrl() {
			return HQMusicUrl;
		}
		public void setHQMusicUrl(String hQMusicUrl) {
			HQMusicUrl = hQMusicUrl;
		}
		public String getThumbMediaId() {
			return ThumbMediaId;
		}
		public void setThumbMediaId(String thumbMediaId) {
			ThumbMediaId = thumbMediaId;
		}
	}
	
	@Override
	public String toString() {
		return "WxMessage [ToUserName=" + ToUserName + ", FromUserName="
				+ FromUserName + ", CreateTime=" + CreateTime + ", MsgType="
				+ MsgType + ", MsgId=" + MsgId + ", Content=" + Content
				+ ", Image=" + Image + ", Voice=" + Voice + ", Video=" + Video
				+ ", Music=" + Music + ", Location_X=" + Location_X
				+ ", Location_Y=" + Location_Y + ", Scale=" + Scale
				+ ", Label=" + Label + ", Title=" + Title + ", Description="
				+ Description + ", Url=" + Url + "]";
	}
}
