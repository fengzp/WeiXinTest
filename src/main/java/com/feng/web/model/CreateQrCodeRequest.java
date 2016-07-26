package com.feng.web.model;

public class CreateQrCodeRequest {

	private Integer expire_seconds;
	private String action_name;
	private Action_Info action_info;
	
	public Integer getExpire_seconds() {
		return expire_seconds;
	}
	public void setExpire_seconds(Integer expire_seconds) {
		this.expire_seconds = expire_seconds;
	}
	public String getAction_name() {
		return action_name;
	}
	public void setAction_name(String action_name) {
		this.action_name = action_name;
	}
	public Action_Info getAction_info() {
		return action_info;
	}
	public void setAction_info(Action_Info action_info) {
		this.action_info = action_info;
	}

	public static class Action_Info{
		private Scene scene;
		public Action_Info(Scene scene) {
			super();
			this.scene = scene;
		}
		public Scene getScene() {
			return scene;
		}
		public void setScene(Scene scene) {
			this.scene = scene;
		}
	}
	
	public static class Scene{
		private int scene_id;
		private String scene_str;
		public Scene(int scene_id, String scene_str) {
			super();
			this.scene_id = scene_id;
			this.scene_str = scene_str;
		}
		public int getScene_id() {
			return scene_id;
		}
		public void setScene_id(int scene_id) {
			this.scene_id = scene_id;
		}
		public String getScene_str() {
			return scene_str;
		}
		public void setScene_str(String scene_str) {
			this.scene_str = scene_str;
		}
	}
}
