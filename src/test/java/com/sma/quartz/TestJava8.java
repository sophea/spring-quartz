package com.sma.quartz;

import lombok.Builder;
import lombok.Data;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author sopheamak
 * @date : 2020-07-16
 **/
public class TestJava8 {

    public static void main(String[] args) {

        People p1 = People.builder()
                .age(37)
                .name("Sophea")
                .sex("M")
                .build();

        People p2 = new People("Nary", "F", 35);
        People p3 = new People("Kunkun", "M", 10);
        People p4 = new People("VuthVuth", "M", 4);


        List<People> list = Arrays.asList(p1, p2, p3, p4);

        //spil by F M into 2 lists

        Map<Boolean, List<People>> mapList = list.stream().collect(Collectors.groupingBy(item -> "M".equals(item.getSex())));

       //System.out.println(mapList.get(false));


//        List<People> mList = new ArrayList<>();
//        List<People> fList = new ArrayList<>();
//
//        list.forEach(splitBy(item -> item.getSex().equals("M"), mList::add, fList::add));

      //  System.out.println(fList);


        final Map<String, Object> m1 = new HashMap<>();
        m1.put("name", "Sophea");
        m1.put("age", 34);
        m1.put("sex", "M");


        final Map<String, Object> m2 = new HashMap<>();
        m2.put("name", "Nary");
        m2.put("age", 34);
        m2.put("sex", "F");
        final Map<String, Object> m3 = new HashMap<>();
        m3.put("name", "Kunkun");
        m3.put("age", 10);
        m3.put("sex", "M");

        List<Map<String,Object>> list2 = Arrays.asList(m1,m2,m3);
        Map<Boolean, List<Map<String,Object>>> mapList2= list2.stream().collect(Collectors.groupingBy(item ->
        {
            return "M".equals(item.get("sex"));
        }));

        System.out.println(mapList2.get(true));

        List<Map<String,Object>> mList = new ArrayList<>();
        List<Map<String,Object>> fList = new ArrayList<>();

       list2.forEach(splitBy(item -> item.get("sex").equals("M"), mList::add, fList::add));

    }


    private static <T> Consumer<T> splitBy(
            Predicate<T> condition,
            Consumer<T> action1,
            Consumer<T> action2
           ) {
        return n -> {
            if (condition.test(n)) {
                action1.accept(n);
            } else {
                action2.accept(n);
            }
        };
    }


    @Data
    @Builder
    private static class People {
        public String name;
        public String sex;
        public int age;

    }
}
