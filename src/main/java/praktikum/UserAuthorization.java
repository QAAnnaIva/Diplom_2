package praktikum;


public class UserAuthorization {

    public String getAccessToken() {
        return accessToken;
    }
    public String getRefreshToken() {return refreshToken; }

    private String accessToken;
    private String refreshToken;

}
