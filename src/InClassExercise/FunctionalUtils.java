package InClassExercise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

interface MyFunctionInterface {
    Number call(Integer n);
}


class FunctionalUtils {
    static void forEach(List<Integer> l, MyFunctionInterface f) {
        // implement this
        l.forEach((n) -> { System.out.println(f.call(n)); });
    }

    static List<Number> map(MyFunctionInterface f, List<Integer> l) {
        // implement this
        List<Number> output = new ArrayList<>();
        l.forEach((n) ->{
            output.add(f.call(n));
        });
        return output;
    }

    static List<Integer> filter(List<Integer> l, MyFunctionInterface f) {
        // Implement this
        List<Integer> output = new ArrayList<>();
        l.forEach((n) ->{
            if (f.call(n) != null){
                output.add(n);
            }

        });
        return output;
    }

}

class Main {
    public static void main(String[] args) {
        List<Integer> myList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        // Call forEach with a function that prints out the cube of the integer
        FunctionalUtils a = new FunctionalUtils();
        MyFunctionInterface a1 = n -> n*n*n;
        a.forEach(myList, a1);

        // Call forEach with a function that prints out the inverse the integer, as a float
        MyFunctionInterface a2 = n -> (n==0)? 0: (float)1/n;
        a.forEach(myList,a2);

        // Call map with a function that returns the cube of the integer
        List<Number> mapResult1 = a.map(a1,myList);
        System.out.println(mapResult1);

        // Call map with a function that returns the inverse of the integer, as a float
        List<Number> mapResult2 = a.map(a2,myList);
        System.out.println(mapResult2);

        // Call filter with a function to filter out the odd numbers

        List<Integer> filterResult1= a.filter(myList, new MyFunctionInterface() {
            public Number call(Integer n) {
                if (n%2 == 0){
                    return n;
                }
                return null;
            }
        });
        System.out.println(filterResult1);

        // Call filter with a function to filter out the multiples of 3
        List<Integer> filterResult2= a.filter(myList, new MyFunctionInterface() {
            public Number call(Integer n) {
                if (n%3 != 0){
                    return n;
                }
                return null;
            }
        });
        System.out.println(filterResult2);

    }
}
