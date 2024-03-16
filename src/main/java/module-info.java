module com.os.cpu_scheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;
    requires VirtualizedFX;
                            
    opens com.os.cpu_scheduler to javafx.fxml;
    exports com.os.cpu_scheduler;
    exports com.os.cpu_scheduler.controller;
    opens com.os.cpu_scheduler.controller to javafx.fxml;
}