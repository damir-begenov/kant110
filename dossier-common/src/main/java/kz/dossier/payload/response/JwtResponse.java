package kz.dossier.payload.response;

import java.util.List;

public class JwtResponse {
  private String accessToken;
  private String refreshToken;
  private String username;
  private String email;
  private List<String> roles;


  public JwtResponse(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }
  public JwtResponse(String accessToken, String refresh, String username, String email, List<String> roles) {
    this.accessToken = accessToken;
    this.refreshToken = refresh;
    this.username = username;
    this.email = email;
    this.roles = roles;
  }
  public String getAccessToken() {
      return accessToken;
  }
  public void setAccessToken(String accessToken) {
      this.accessToken = accessToken;
  }
  

  public String getEmail() {
      return email;
  }
  public String getRefreshToken() {
      return refreshToken;
  }
  public List<String> getRoles() {
      return roles;
  }
  public String getUsername() {
      return username;
  }
  public void setEmail(String email) {
      this.email = email;
  }
  public void setRefreshToken(String refreshToken) {
      this.refreshToken = refreshToken;
  }
  public void setRoles(List<String> roles) {
      this.roles = roles;
  }
  public void setUsername(String username) {
      this.username = username;
  }
}
