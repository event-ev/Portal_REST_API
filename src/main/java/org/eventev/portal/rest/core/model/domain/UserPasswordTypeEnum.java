package org.eventev.portal.rest.core.model.domain;

public enum UserPasswordTypeEnum {

	SSHA512;

	public abstract class Password {

		private UserPasswordTypeEnum type;

		public Password(UserPasswordTypeEnum type) {
			this.type = type;
		}

		public UserPasswordTypeEnum getType() {
			return type;
		}

		public void setType(UserPasswordTypeEnum type) {
			this.type = type;
		}

	}

	public class SSHA512 extends Password {

		private String hash;
		private String salt;

		public SSHA512(String password) {
			// TODO: Generate salt and hash
			this("", "");
		}
		
		public SSHA512(String hash, String salt) {
			super(UserPasswordTypeEnum.SSHA512);
			this.hash = hash;
			this.salt = salt;
		}

		public String getHash() {
			return hash;
		}

		public void setHash(String hash) {
			this.hash = hash;
		}

		public String getSalt() {
			return salt;
		}

		public void setSalt(String salt) {
			this.salt = salt;
		}
		
	}

}
