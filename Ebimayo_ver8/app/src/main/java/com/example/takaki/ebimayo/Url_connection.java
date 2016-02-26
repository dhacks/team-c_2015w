package com.example.takaki.ebimayo;

    import java.io.BufferedReader;
    import java.io.InputStreamReader;
    import java.io.OutputStreamWriter;
    import java.net.HttpURLConnection;
    import java.net.URL;

    public class Url_connection {
        static Common global;
        static boolean downLoaded;
        static boolean upLoaded;
        static String downLoadedPassword;


        Url_connection(Common g){
            global = g;
        }


        static void downLoadFavoriteCircle(final int n){
            downLoaded = false;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    downLoaded = urlConnectionFavoriteCircle(n);
                }
            }).start();

            while (!downLoaded);

        }

        static boolean urlConnectionFavoriteCircle(int n){
            try {
                String url_line = global.SERVER_URL + "/?purpose=6&circle_id=" + global.id_favorite[n];
                System.out.println(url_line);
                URL url = new URL(url_line);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                con.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String data = reader.readLine();
                global.data_fav[n].title = data;

                data = reader.readLine();
                global.data_fav[n].field = Integer.parseInt(data);
                data = reader.readLine();
                global.data_fav[n].school = Integer.parseInt(data);

                reader.close();
                con.disconnect();

            }catch (Exception e){
                e.printStackTrace();
            }
            return true;
        }

        static boolean urlConnectionFavoriteEvent(int n){
            try{
                String url_line = global.SERVER_URL + "/?purpose=1&circle_id=" + global.id_favorite[n];
                URL url = new URL(url_line);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                System.out.println("a");
                con.connect();
                System.out.println("b");
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String data;
                System.out.println("c");
                while((data = reader.readLine()) != null){
                    System.out.println(data);
                    global.data_fav[n].event_month[global.fav_num] = Integer.parseInt(data);

                    data = reader.readLine();
                    global.data_fav[n].event_day[global.fav_num] = Integer.parseInt(data);

                    data = reader.readLine();
                    global.data_fav[n].event_time[global.fav_num] = data;

                    data = reader.readLine();
                    global.data_fav[n].event_place[global.fav_num] = data;

                    data = reader.readLine();
                    global.data_fav[n].event_title[global.fav_num] = data;

                    data = reader.readLine();
                    global.data_fav[n].event_detail[global.fav_num] = data;
                    global.fav_num++;
                }

                reader.close();
                con.disconnect();


            }catch (Exception e){
                e.printStackTrace();
            }

            return true;
        }

        static  void downLoadFavoriteEvent(final int n){
            downLoaded = false;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    downLoaded = urlConnectionFavoriteEvent(n);
                }
            }).start();

            while (!downLoaded);

        }

        static void getSearchResult(final int field,final int school){
            downLoaded = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    downLoaded = urlConnectionGetSearchResult(field, school);
                }
            }).start();
            while (!downLoaded);

        }

        static boolean urlConnectionGetSearchResult(int field, int school){
            try{

                String url_line = global.SERVER_URL + "/?purpose=2&category=" + field + "&school=" + school;
                URL url = new URL(url_line);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                con.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String data;
                int n = 0;
                while((data = reader.readLine()) != null){
                    global.data[n].id = Integer.parseInt(data);
                    data = reader.readLine();
                    global.data[n].title = data;
                    data = reader.readLine();
                    global.data[n].field = Integer.parseInt(data);
                    data = reader.readLine();
                    global.data[n].school = Integer.parseInt(data);
                    n++;
                    global.search_result_num++;
                }
                reader.close();
                con.disconnect();

            }catch (Exception e){
                e.printStackTrace();
            }

            return true;
        }

        static String getPassword(final String login_id){
            downLoaded = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    downLoaded = urlConnectionEvent(login_id);
                }
            }).start();
            while (!downLoaded);

            return downLoadedPassword;
        }

        static boolean urlConnectionEvent(String login_id){
            try{
                String url_line = global.SERVER_URL + "/?purpose=5&login_id=" + login_id;
                URL url = new URL(url_line);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                con.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String data = reader.readLine();
                if(data != null){
                    downLoadedPassword = data;
                }else{
                    downLoadedPassword = null;
                }
                data = reader.readLine();
                global.server_id = Integer.parseInt(data);
                System.out.println(global.server_id);

                reader.close();
                con.disconnect();

            }catch (Exception e){
                e.printStackTrace();
            }

            return true;
        }

        static void downLoadProfilePage(final int id){
            downLoaded = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    downLoaded = urlConnectionDownLoadProfilePage(id);
                }
            }).start();
            while(!downLoaded);

        }

        static boolean urlConnectionDownLoadProfilePage(int id){
            try{
                String url_line = global.SERVER_URL + "/?purpose=3&circle_id=" + id;
                URL url = new URL(url_line);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                con.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String data = reader.readLine();
                global.page.title = data;
                data = reader.readLine();
                global.page.field = Integer.parseInt(data);
                data = reader.readLine();
                global.page.school = Integer.parseInt(data);
                data = reader.readLine();
                global.page.place = data;
                data = reader.readLine();
                global.page.week = data;
                data = reader.readLine();
                global.page.time_st = data;
                data = reader.readLine();
                global.page.time_et = data;
                data = reader.readLine();
                global.page.member = data;
                data = reader.readLine();
                global.page.mail = data;
                data = reader.readLine();
                global.page.introduction = data;

                reader.close();
                con.disconnect();

            }catch (Exception e){
                e.printStackTrace();
            }

            return true;
        }

        static void upLoadProviderPage(){
            upLoaded = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    upLoaded = urlConnectionUploadProviderPage();
                }
            }).start();
            while (!upLoaded);
        }

        static boolean urlConnectionUploadProviderPage(){
            try{
                String urlParameter = "purpose=1&circle_id=" + global.server_id
                        + "&school=" + global.server_school + "&place=" + global.server_place + "&activity_date=" + global.server_week
                        + "&start_time=" + global.server_time_st + "&end_time" + global.server_time_et
                        + "&comment=" + global.server_event_detail;
                URL url = new URL(global.SERVER_URL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                con.setDoOutput(true);

                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(urlParameter);
                writer.flush();
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                line = reader.readLine();
                System.out.println(line);
                writer.close();
                reader.close();
                con.disconnect();

            }catch (Exception e){
                e.printStackTrace();
            }
            return true;
        }

        static void downLoadProviderPage(final int id){
            downLoaded = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    downLoaded = urlConnectionDownLoadProviderPage(id);
                }
            }).start();
            while(!downLoaded);

        }

        static boolean urlConnectionDownLoadProviderPage(int id){
            try{
                String url_line = global.SERVER_URL + "/?purpose=3&circle_id=" + id;
                URL url = new URL(url_line);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                con.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String data = reader.readLine();
                global.page.title = data;
                data = reader.readLine();
                global.page.field = Integer.parseInt(data);
                data = reader.readLine();
                global.page.school = Integer.parseInt(data);
                data = reader.readLine();
                global.page.place = data;
                data = reader.readLine();
                global.page.week = data;
                data = reader.readLine();
                global.page.time_st = data;
                data = reader.readLine();
                global.page.time_et = data;
                data = reader.readLine();
                global.page.member = data;
                data = reader.readLine();
                global.page.mail = data;
                data = reader.readLine();
                global.page.introduction = data;

                reader.close();
                con.disconnect();

            }catch (Exception e){
                e.printStackTrace();
            }

            return true;
        }

        static void downLoadProviderEvents(final int id){
            downLoaded = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    downLoaded = urlConnectionDownLoadProviderEvents(id);
                }
            }).start();

            while (!downLoaded);
        }

        static boolean urlConnectionDownLoadProviderEvents(int id){
            try{
                String url_line = global.SERVER_URL + "/?purpose=7&circle_id=" + id;
                URL url = new URL(url_line);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                con.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String data;
                while((data = reader.readLine()) != null){
                    global.page.event_id[global.event_num] = Integer.parseInt(data);
                    data = reader.readLine();
                    global.page.event_month[global.event_num] = Integer.parseInt(data);
                    data = reader.readLine();
                    global.page.event_day[global.event_num] = Integer.parseInt(data);
                    data = reader.readLine();
                    global.page.event_time[global.event_num] = data;
                    data = reader.readLine();
                    global.page.event_place[global.event_num] = data;
                    data = reader.readLine();
                    global.page.event_title[global.event_num] = data;
                    data = reader.readLine();
                    global.page.event_detail[global.event_num] = data;
                    global.event_num++;
                }

                reader.close();
                con.disconnect();

            }catch (Exception e){
                e.printStackTrace();
            }

            return true;
        }

    }


