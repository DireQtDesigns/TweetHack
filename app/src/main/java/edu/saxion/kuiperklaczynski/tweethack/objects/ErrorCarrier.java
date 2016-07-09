package edu.saxion.kuiperklaczynski.tweethack.objects;

/**
 * Created by Robin on 9-7-2016.
 */
public class ErrorCarrier extends Tweet {
    int errorCode;

    /**
     * this method is used purely for carrying error codes from a DoInBackground method to onPostExecute so we can access the UI thread again
     * @param errorCode errorcode
     */
    public ErrorCarrier(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
