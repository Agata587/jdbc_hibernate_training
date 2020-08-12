package jdbc.domain;

/*
Classic Cars
Motorcycles
Planes
Ships
Trains
Trucks
Buses
Vintage Cars
 */


public enum ProductLine {


    CLASSIC_CARS {    // nie może to być pisane jak w orginale oddzielnie dlatego dorze jest zrobić im Override i przekazać orginalną pisownię w to.String
        @Override
        public String toString() {
            return "Classic Cars";
        }
    },   //pamiętaj o przecinku

    MOTORCYCLES {  //małych liter też nie może być więc w sumie dla każdego trzeba to zrobić
        @Override
        public String toString() {
            return "Motorcycles";
        }
    },

    PLANES{
        @Override
        public String toString() {
            return "Planes";
        }
    },

    SHIPS {
        @Override
        public String toString() {
            return "Ships";
        }
    },

    TRAINS {
        @Override
        public String toString() {
            return "Trains";
        }
    },


    TRUCKS {
        @Override
        public String toString() {
            return "Trucks";
        }
    },

    BUSES {
        @Override
        public String toString() {
            return "Buses";
        }
    },

    VINATAGE_CARS{
        @Override
        public String toString() {
            return "Vintage Cars";
        }
    }







}