 

 getJsonAlat("http://192.168.43.144/TA/getPeralatan.php");


 private void getJsonAlat(final String urlWebService)
    {
        class GetJsonAlat extends AsyncTask<Void,Void,String>
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    panggilAlat(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try{
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null)
                    {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();

                }
                catch (Exception e)
                {
                    return null;
                }
            }
        }
        GetJsonAlat getJson = new GetJsonAlat();
        getJson.execute();
    }
    private void panggilAlat(String json) throws JSONException
    {
        JSONArray jsonArray = new JSONArray(json);

        for ( int a = 0; a < jsonArray.length(); a++)
        {
            JSONObject obj = jsonArray.getJSONObject(a);
            alats.add(new Alat(obj.getInt("id"),obj.getInt("kode_alat"),obj.getString("merk"),obj.getString("namaalat"),obj.getInt("tahunproduksi")));
        }

        ArrayAdapter<Alat> spinadapter = new ArrayAdapter<Alat>(KomplainAlatActivity.this,R.layout.support_simple_spinner_dropdown_item,alats);
        spinadapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinalat1.setAdapter(spinadapter);
        spinalat2.setAdapter(spinadapter);
    }
