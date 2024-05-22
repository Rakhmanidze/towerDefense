module cz.cvut.fel.pjv.semak_pjv {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;
    requires jdk.accessibility;
    requires java.logging;

    opens cz.cvut.fel.pjv.semak_pjv to javafx.fxml;
    exports cz.cvut.fel.pjv.semak_pjv;
    exports cz.cvut.fel.pjv.semak_pjv.main;
    opens cz.cvut.fel.pjv.semak_pjv.main to javafx.fxml;
    exports cz.cvut.fel.pjv.semak_pjv.helperMethods;
    opens cz.cvut.fel.pjv.semak_pjv.helperMethods to javafx.fxml;
}