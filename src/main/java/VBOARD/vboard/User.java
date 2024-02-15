/*
 *	Author: Vitaly Vakhteev
 *  Date: 01/22/24
 */

package VBOARD.vboard;

import org.mindrot.jbcrypt.BCrypt;

public class User {
	String usr;
	String hashedPwd;

	public User() {
		usr = "";
		hashedPwd = "";
	}

	public User(String usr, String pwd, boolean isHashed) {
		this.usr = usr;
		this.hashedPwd = isHashed ? pwd : BCrypt.hashpw(pwd, BCrypt.gensalt());
	}

	public String getUsername(){
		return usr;
	}

	public boolean check(String usr, String pwd){
		if (usr == null) {
			return false;
		}
		return this.usr.equals(usr) && BCrypt.checkpw(pwd, hashedPwd);
	}

	public boolean setPassword(String oldPass, String newPass) {
		if (this.hashedPwd.isEmpty() || !BCrypt.checkpw(oldPass, hashedPwd)) {
			return false;
		}
		hashedPwd = BCrypt.hashpw(newPass, BCrypt.gensalt());
		return true;
	}
}
