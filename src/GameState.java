// Enum: กำหนดสถานะของแต่ละฉากในเกม
public enum GameState {
    MAIN_MENU,
    PROLOGUE,
    OFFICE,
    SNOW_PATH_TO_WINTERHALL,  // เดินจากออฟฟิศ → หน้าโบสถ์
    CHURCH_HALL,              // พบ Blindfold + Cypher + เรเน่ในโบสถ์
    SNOW_PATH_RETURN,         // เดินออกจากโบสถ์พร้อมเรเน่ → สะพาน
    FINAL_CHOICE,             // จุดตัดสินใจสุดท้าย (สะพาน)
    ENDING_ESCAPE,            // Ending 1: หนีไปด้วยกัน (reneAffection >= 3)
    ENDING_CAPTURE            // Ending 2: จับส่งองค์กร
}
