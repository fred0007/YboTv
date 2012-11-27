package fr.ybo.ybotv.android.service;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import fr.ybo.ybotv.android.exception.YboTvException;
import fr.ybo.ybotv.android.modele.Channel;
import fr.ybo.ybotv.android.modele.Programme;
import fr.ybo.ybotv.android.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.util.List;

public class YboTvService {

    private static final YboTvService instance = new YboTvService();

    private static final String SERVEUR = "http://ybo-tv.appspot.com/";

    private static final String CHANNEL_SERVICE_URL = "data/channel/";

    private static final String PROGRAMME_SERVICE_URL = "data/programme/";

    private static final String CHANNEL_PARAMETER = "channel/";

    private YboTvService() {
    }

    public static YboTvService getInstance() {
        return instance;
    }

    public <T> List<T> getObjects(String url, TypeToken<List<T>> typeToken) {
        try {
            HttpClient client = HttpUtils.getHttpClient();
            HttpUriRequest request = new HttpGet(SERVEUR + CHANNEL_SERVICE_URL);
            HttpResponse reponse = client.execute(request);
            Reader reader = new InputStreamReader(reponse.getEntity().getContent());
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            List<T> objects = gson.fromJson(reader, typeToken.getType());
            reader.close();
            return objects;
        } catch (MalformedURLException e) {
            throw new YboTvException(e);
        } catch (IOException e) {
            throw new YboTvException(e);
        }
    }

    public List<Channel> getChannels()  {
        return getObjects(SERVEUR + CHANNEL_SERVICE_URL, new TypeToken<List<Channel>>(){});
    }

    public List<Programme> getProgrammes(Channel channel) {
        return getObjects(SERVEUR + PROGRAMME_SERVICE_URL + CHANNEL_PARAMETER + channel.getId(), new TypeToken<List<Programme>>(){});
    }
}
