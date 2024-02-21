/*
 *	Author: Vitaly Vakhteev
 *  Date: 01/22/24
 */

package VBOARD.vboard;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.persistence.*;

/**
 * The User class represents a user in the system.
 * It includes details like id, username and hashed password.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	/**
	 * The unique ID of the user.
	 */
	private Long id;

	@Column(unique = true, nullable = false)
	/**
	 * The unique username of the user.
	 */
	private String username;

	@Column(name = "hashed_pwd", nullable = false)
	/**
	 * The hashed password of the user.
	 */
	private String hashedPwd;

	/**
	 * Constructs a new User with the given username and password.
	 * If isHashed is true, the password is stored as is, otherwise it is hashed before storing.
	 *
	 * @param username the username of the user
	 * @param pwd the password of the user
	 * @param isHashed whether the password is already hashed
	 */
	public User(String username, String pwd, boolean isHashed) {
		this.username = username;
		this.hashedPwd = isHashed ? pwd : BCrypt.hashpw(pwd, BCrypt.gensalt());
	}

	/**
	 * Checks if the given username and password match the user's username and password.
	 *
	 * @param usr the username to check
	 * @param pwd the password to check
	 * @return true if the username and password match, false otherwise
	 */
	public boolean check(String usr, String pwd){
		return this.username.equals(usr) && BCrypt.checkpw(pwd, this.hashedPwd);
	}

	/**
	 * Changes the user's password if the old password is correct.
	 *
	 * @param oldPass the old password
	 * @param newPass the new password
	 * @return true if the password was changed, false otherwise
	 */
	public boolean setPassword(String oldPass, String newPass) {
		if (this.hashedPwd.isEmpty() || !BCrypt.checkpw(oldPass, this.hashedPwd)) {
			return false;
		}
		this.hashedPwd = BCrypt.hashpw(newPass, BCrypt.gensalt());
		return true;
	}
}