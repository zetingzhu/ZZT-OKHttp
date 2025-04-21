package com.example.zzt.okhttp.entity;

import java.util.List;

/**
 * @author: zeting
 * @date: 2025/4/11
 */
public class GaodeWeather {

    private String status;

    private String count;

    private String info;

    private String infocode;

    private List<ForecastsDTO> forecasts;

    private List<LivesDTO> lives;

    public static class LivesDTO {

        private String province;

        private String city;

        private String adcode;

        private String weather;

        private String temperature;

        private String winddirection;

        private String windpower;

        private String humidity;

        private String reporttime;

        private String temperature_float;

        private String humidity_float;

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAdcode() {
            return adcode;
        }

        public void setAdcode(String adcode) {
            this.adcode = adcode;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getWinddirection() {
            return winddirection;
        }

        public void setWinddirection(String winddirection) {
            this.winddirection = winddirection;
        }

        public String getWindpower() {
            return windpower;
        }

        public void setWindpower(String windpower) {
            this.windpower = windpower;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getReporttime() {
            return reporttime;
        }

        public void setReporttime(String reporttime) {
            this.reporttime = reporttime;
        }

        public String getTemperature_float() {
            return temperature_float;
        }

        public void setTemperature_float(String temperature_float) {
            this.temperature_float = temperature_float;
        }

        public String getHumidity_float() {
            return humidity_float;
        }

        public void setHumidity_float(String humidity_float) {
            this.humidity_float = humidity_float;
        }
    }


    public static class ForecastsDTO {

        private String city;

        private String adcode;

        private String province;

        private String reporttime;

        private List<CastsDTO> casts;

        public static class CastsDTO {

            private String date;

            private String week;

            private String dayweather;

            private String nightweather;

            private String daytemp;

            private String nighttemp;

            private String daywind;

            private String nightwind;

            private String daypower;

            private String nightpower;

            private String daytemp_float;

            private String nighttemp_float;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getDayweather() {
                return dayweather;
            }

            public void setDayweather(String dayweather) {
                this.dayweather = dayweather;
            }

            public String getNightweather() {
                return nightweather;
            }

            public void setNightweather(String nightweather) {
                this.nightweather = nightweather;
            }

            public String getDaytemp() {
                return daytemp;
            }

            public void setDaytemp(String daytemp) {
                this.daytemp = daytemp;
            }

            public String getNighttemp() {
                return nighttemp;
            }

            public void setNighttemp(String nighttemp) {
                this.nighttemp = nighttemp;
            }

            public String getDaywind() {
                return daywind;
            }

            public void setDaywind(String daywind) {
                this.daywind = daywind;
            }

            public String getNightwind() {
                return nightwind;
            }

            public void setNightwind(String nightwind) {
                this.nightwind = nightwind;
            }

            public String getDaypower() {
                return daypower;
            }

            public void setDaypower(String daypower) {
                this.daypower = daypower;
            }

            public String getNightpower() {
                return nightpower;
            }

            public void setNightpower(String nightpower) {
                this.nightpower = nightpower;
            }

            public String getDaytemp_float() {
                return daytemp_float;
            }

            public void setDaytemp_float(String daytemp_float) {
                this.daytemp_float = daytemp_float;
            }

            public String getNighttemp_float() {
                return nighttemp_float;
            }

            public void setNighttemp_float(String nighttemp_float) {
                this.nighttemp_float = nighttemp_float;
            }
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAdcode() {
            return adcode;
        }

        public void setAdcode(String adcode) {
            this.adcode = adcode;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getReporttime() {
            return reporttime;
        }

        public void setReporttime(String reporttime) {
            this.reporttime = reporttime;
        }

        public List<CastsDTO> getCasts() {
            return casts;
        }

        public void setCasts(List<CastsDTO> casts) {
            this.casts = casts;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public List<ForecastsDTO> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<ForecastsDTO> forecasts) {
        this.forecasts = forecasts;
    }

    public List<LivesDTO> getLives() {
        return lives;
    }

    public void setLives(List<LivesDTO> lives) {
        this.lives = lives;
    }

    @Override
    public String toString() {
        return "GaodeWeather{" +
                "status='" + status + '\'' +
                ", count='" + count + '\'' +
                ", info='" + info + '\'' +
                ", infocode='" + infocode + '\'' +
                ", forecasts=" + forecasts +
                ", lives=" + lives +
                '}';
    }
}
