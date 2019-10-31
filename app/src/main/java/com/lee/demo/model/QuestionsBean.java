package com.lee.demo.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by ChrisLee on 2019/9/10.
 */

public class QuestionsBean   implements Serializable{
    private static final long serialVersionUID = 3325392351882664930L;
    /**
     * exam_name : 2019南京市查验员初级考试
     * appointment_id : 24
     * start_time : 2019-09-10 08:00:00
     * end_time : 2019-09-06 10:30:00
     * start_time_span:1568145600
     * end_time_span:1567809000
     * notes:1.考试总题目数：10道；2.考试开始时间：2019-09-10 08:00:00;3.考试结束时间：2019-09-06 10:30:00;4.考试时间到系统自动交卷，请考生合理分配时间。
     * total_num:10
     * data : [{"id":184,"type":0,"content":"对登记后上道路行驶的机动车，应当依照法律、行政法规的规定，根据车辆用途、载客载货数量、___ 等不同情况，定期进行安全技术检验。","option":["A、使用次数","B、使用年限","C、行驶里程 ","D、维修次数"],"answer":"B","create_time":"2019-06-26T15:35:34","grade":1},{"id":313,"type":1,"content":"公安机关交通管理部门应当依法对机动车安全技术检验机构出具虚假检验结果情况进行监督管理。","option":null,"answer":"正确","create_time":"2019-07-08T11:49:03","grade":2},{"id":95,"type":0,"content":"对登记后上道路行驶的机动车，应当依照法律、行政法规的规定，根据车辆用途、载客载货数量、___ 等不同情况，定期进行安全技术检验。","option":["A、使用次数","B、使用年限","C、行驶里程 ","D、维修次数"],"answer":"B","create_time":"2019-06-04T11:32:12","grade":1},{"id":100,"type":1,"content":"公安机关交通管理部门应当依法对机动车安全技术检验机构出具虚假检验结果情况进行监督管理。","option":null,"answer":"正确","create_time":"2019-06-04T11:32:12","grade":2},{"id":101,"type":1,"content":" 公路监督检查的专用车辆，应当依照公路法的规定，设置统一的标志和示警灯。","option":null,"answer":"正确","create_time":"2019-06-04T11:32:12","grade":1},{"id":111,"type":2,"content":"___应当按照国家机动车安全技术检验标准对机动车进行检验，对检验结果承担法律责任。","option":["A、公安车辆管理机关交通管理部门","B、机动车安全技术检验机构","C、质量技术监督部门","D、车辆维修保养企业"],"answer":"BD","create_time":"2019-06-04T11:32:14","grade":1},{"id":115,"type":1,"content":" 公路监督检查的专用车辆，应当依照公路法的规定，设置统一的标志和示警灯。","option":null,"answer":"正确","create_time":"2019-06-04T11:32:14","grade":1},{"id":120,"type":1,"content":"公安机关交通管理部门、机动车安全技术检验机构不得要求机动车到指定的场所进行维修、保养。","option":null,"answer":"正确","create_time":"2019-06-04T11:32:15","grade":2},{"id":323,"type":0,"content":"机动车安全技术检验机构不按照国家安全技术标准进行检验，出具虚假检验结果的，处检验费用___罚款。","option":["A、5倍以上10倍以下","B、5倍以上20倍以下","C、1倍以上20倍以下","D、1倍以上5倍以下"],"answer":"A","create_time":"2019-07-08T11:49:04","grade":2},{"id":330,"type":2,"content":"机动车安全技术检验机构不按照国家安全技术标准进行检验，出具虚假检验结果的，处检验费用___罚款。","option":["A、5倍以上10倍以下","B、5倍以上20倍以下","C、1倍以上20倍以下","D、1倍以上5倍以下"],"answer":"AC","create_time":"2019-07-08T11:49:30","grade":1},{"id":327,"type":1,"content":"公安机关交通管理部门应当依法对机动车安全技术检验机构出具虚假检验结果情况进行监督管理。","option":null,"answer":"正确","create_time":"2019-07-08T11:49:04","grade":2}]
     * code : 200
     */

    private String exam_name;
    private String appointment_id;
    private String start_time;
    private String end_time;
    private String start_time_span;//考试开始时间：单位：s
    private String end_time_span;//考试结束时间：单位：s
    private String notes;//考试通知文案
    private String total_num;//总题目数量

    private List<DataBean> data;

    public String getStart_time_span() {
        return start_time_span;
    }

    public void setStart_time_span(String start_time_span) {
        this.start_time_span = start_time_span;
    }

    public String getEnd_time_span() {
        return end_time_span;
    }

    public void setEnd_time_span(String end_time_span) {
        this.end_time_span = end_time_span;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTotal_num() {
        return total_num;
    }

    public void setTotal_num(String total_num) {
        this.total_num = total_num;
    }

    public String getExam_name() {
        return exam_name;
    }

    public void setExam_name(String exam_name) {
        this.exam_name = exam_name;
    }

    public String getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{

        private static final long serialVersionUID = -8300494004155432235L;
        /**
         * id : 184
         * type : 0
         * content : 对登记后上道路行驶的机动车，应当依照法律、行政法规的规定，根据车辆用途、载客载货数量、___ 等不同情况，定期进行安全技术检验。
         * option : ["A、使用次数","B、使用年限","C、行驶里程 ","D、维修次数"]
         * answer : B
         * create_time : 2019-06-26T15:35:34
         * grade : 1
         */

        private String id;
        private int type;
        private String content;
        private String answer;
        private String create_time;
        private int grade;
        private List<String> option;
        private int sequence=1;//題目序号--自定义
        private TreeMap<String, String> userAnswerMap = new TreeMap<>();//用户的答案  key: A或B或C或D ，value: A、使用次数  或者 A、正确

        public int getSequence() {
            return sequence;
        }

        public void setSequence(int sequence) {
            this.sequence = sequence;
        }

        public TreeMap<String, String> getUserAnswerMap() {
            return userAnswerMap;
        }
        public String getUserAnswerByMap() {//根据 用户答案集合 得到传给后台的答案
            String answer = "";//单个题目的答案
            for (Iterator<Map.Entry<String, String>> it = userAnswerMap.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, String> entry = it.next();
                //0单选题 1判断题 2多选题
                if (1 == getType()) {
                    answer+=entry.getValue().substring(entry.getValue().length()-2);//倒数2个字符串
                } else {//
                    answer+=entry.getKey();
                }
            }
            return answer;
        }
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public List<String> getOption() {
            return option;
        }

        public void setOption(List<String> option) {
            this.option = option;
        }
    }
}
