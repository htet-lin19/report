package com.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.jdbc.DataConnection;
import com.opensymphony.xwork2.ActionSupport;

public class ResetPasswordAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String emp_cd;
	private String inputPassword;
	private long atv_code;
	private Connection dbConnection;

	/**
	 * @return the emp_cd
	 */
	public String getEmp_cd() {
		return emp_cd;
	}

	/**
	 * @param emp_cd
	 *            the emp_cd to set
	 */
	public void setEmp_cd(String emp_cd) {
		this.emp_cd = emp_cd;
	}

	/**
	 * @return the inputPassword
	 */
	public String getInputPassword() {
		return inputPassword;
	}

	/**
	 * @param inputPassword
	 *            the inputPassword to set
	 */
	public void setInputPassword(String inputPassword) {
		this.inputPassword = inputPassword;
	}

	/**
	 * @return the atv_code
	 */
	public long getAtv_code() {
		return atv_code;
	}

	/**
	 * @param atv_code
	 *            the atv_code to set
	 */
	public void setAtv_code(long atv_code) {
		this.atv_code = atv_code;
	}

	/**
	 * 初期表示処理
	 */
	public String execute() {
		return SUCCESS;
	}

	/**
	 * check employee code whether exist or not in employee master table
	 * 
	 * @param emp_cd
	 *            String
	 * @return result boolean
	 */
	public boolean validate(String emp_cd) {
		boolean status = false;
		try {
			dbConnection = DataConnection.getConnection();
			PreparedStatement ps = dbConnection.prepareStatement("SELECT * FROM login WHERE emp_cd=?");
			ps.setString(1, emp_cd);
			ResultSet rs = ps.executeQuery();
			status = rs.next();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * generate random activation code and save activation code to employee
	 * master table
	 * 
	 * @return result String(SUCCESS,ERROR)
	 */
	public String generateCode() {
		if (emp_cd == null) {
			return SUCCESS;
		} else if (validate(emp_cd)) {
			long code = Math.round(Math.random() * 1000000);
			try {
				dbConnection = DataConnection.getConnection();
				String email = getEmpEmail(emp_cd, dbConnection);
				saveActivationCode(code, dbConnection);
				sendCode(email, code);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return SUCCESS;
		} else {
			clearActionErrors();
			addActionError("あなたが入力した社員番号とパスワードは、弊社の記録と一致しませんでした。");
			return ERROR;
		}
	}

	/**
	 * get Employee email address by employee code to send activation code
	 * 
	 * @param emp_cd
	 *            String
	 * @param dbConnection
	 *            Connection
	 * @return email String
	 */
	public String getEmpEmail(String emp_cd, Connection dbConnection) {
		try {
			String sql = "SELECT emp_email FROM employeemaster WHERE emp_cd=?";
			PreparedStatement pstmt = dbConnection.prepareStatement(sql);
			pstmt.setString(1, emp_cd);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getString("emp_email");
			else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	/**
	 * save activation code in employee master table
	 * @param code
	 *            long
	 * @param dbConnection
	 *            Connection
	 * @return result String(SUCCESS;ERROR)
	 */
	public String saveActivationCode(long code, Connection dbConnection) {
		try {
			String sql = "UPDATE employeemaster SET activation_code=? WHERE emp_cd=?";
			PreparedStatement pstmt = dbConnection.prepareStatement(sql);
			pstmt.setLong(1, code);
			pstmt.setString(2, emp_cd);

			int status = pstmt.executeUpdate();
			if (status == 1) {
				return SUCCESS;
			} else {
				clearActionErrors();
				addActionError("『メールアドレス』 は間違っています。");
				return SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	/**
	 * send activation code to user
	 * 
	 * @param toEmail
	 *            String
	 * @param code
	 *            long
	 * @return result boolean
	 */
	public boolean sendCode(String toEmail, long code) {
		boolean result = false;
		Properties props = new Properties();
		try {
			props.load(this.getClass().getClassLoader().getResourceAsStream("common.properties"));

			Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(props.getProperty("email"), props.getProperty("password"));
				}
			});
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(props.getProperty("email")));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
			message.setSubject(props.getProperty("subject"));
			message.setText(props.getProperty("body") + code + "\n\n" + props.getProperty("end") + "\n\n"
					+ props.getProperty("link") + "\n\n" + props.getProperty("end"));
			Transport.send(message);
			result = true;
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * check activation code
	 * 
	 * @return result String(SUCCESS;ERROR)
	 */
	public String checkActivationCode() {
		long activation_code = 0;
		try {
			if (validate(emp_cd)) {
				dbConnection = DataConnection.getConnection();
				String sql = "SELECT activation_code FROM employeemaster WHERE emp_cd=?";
				PreparedStatement pstmt = dbConnection.prepareStatement(sql);
				pstmt.setString(1, emp_cd);

				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
					activation_code = rs.getLong("activation_code");

				if (activation_code == atv_code)
					return SUCCESS;
				else {
					clearActionErrors();
					addActionError("『アクティベーションコード』 は間違っています。");
					return ERROR;
				}
			} else {
				clearActionErrors();
				addActionError("あなたが入力した社員番号とパスワードは、弊社の記録と一致しませんでした。");
				return ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	/**
	 * update new password
	 * 
	 * @return result String(SUCCESS;ERROR)
	 */
	public String update() {
		try {
			dbConnection = DataConnection.getConnection();
			String sql = "UPDATE login SET password=? WHERE emp_cd=?";
			PreparedStatement pstmt = dbConnection.prepareStatement(sql);
			pstmt.setString(1, inputPassword);
			pstmt.setString(2, emp_cd);

			int status = pstmt.executeUpdate();

			if (status == 1) {
				sql = "UPDATE employeemaster SET activation_code=? WHERE emp_cd=?";
				pstmt = dbConnection.prepareStatement(sql);
				pstmt.setString(1, null);
				pstmt.setString(2, emp_cd);

				status = pstmt.executeUpdate();
								
				return SUCCESS;
			} else {
				clearActionErrors();
				addActionError("更新できませんでした。");
				return ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
}
