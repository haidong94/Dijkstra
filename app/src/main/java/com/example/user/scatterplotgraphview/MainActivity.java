package com.example.user.scatterplotgraphview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.Random;



public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    //add PointsGraphSeries of DataPoint type
    PointsGraphSeries<DataPoint> xySeries;//tập các điểm trong đồ thị
    //create graphview object
    GraphView mScatterPlot;//đồ thị
    //make xyValueArray global
    ArrayList<Point> xyValueArray;

    private int sodiem,soduong;
    double matrix[][];

    int back[];//lưu đỉnh cha;
    double weight[];//lưu trọng số
    int mark[];//đánh dấu đỉnh

    int i=0;
    int pointDijkstra[]=new int[20];// mang luu vi tri diem dau, diem cuoi duonng can tim
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getIntentFromStartActivity();

        createPoint();
        createScatterPlot();
        createLine();
        createMatrix();
//        CreateDoThi createDoThi=new CreateDoThi();
//        createDoThi.execute();

        eventClickPoint();
    }

    private void eventClickPoint() {
        xySeries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                int vitri = 0;

                for(Point point:xyValueArray){
                    if(point.getX()==dataPoint.getX()&&point.getY()==dataPoint.getY()){
                        vitri=xyValueArray.indexOf(point);
                        pointDijkstra[i]=vitri;
                        i++;
                        break;
                    }
                }

                toastMessage("Điểm thứ: "+vitri+"\n"+"x = " + (int)dataPoint.getX() + "\n" +
                        "y = " + (int)dataPoint.getY() );
                if(i==2)
                {
                    int tongdodaidiemden=0;
                    int tongdodaidiemdi=0;
                    for(int i=0;i<sodiem;i++) {
                        tongdodaidiemden += matrix[pointDijkstra[0]][i];
                        tongdodaidiemdi += matrix[pointDijkstra[1]][i];
                    }

                    if(tongdodaidiemden!=0&& tongdodaidiemdi!=0)
                    {
                        CreateDijkstraTask task = new CreateDijkstraTask(MainActivity.this);
                        task.execute(pointDijkstra[0], pointDijkstra[1]);
                    }
                    else {
                        toastMessage("Không có đường đi");
                    }
                }
                   // createScatterPlot();
            }
        });
    }

    private void createLine() {
        for(int i=0;i<soduong;i++){
            //random để lấy 2 điểm không trùng nhau
            Random rnd1 = new Random();
            int stt1,stt2;
            stt1 = rnd1.nextInt(xyValueArray.size());
            do {
                stt2 = rnd1.nextInt(xyValueArray.size());
            }
            while(stt1 ==stt2);

            //xac định điểm đầu và cuối của đường thẳng(vì chỉ nối từ trái qua phải)
            DataPoint dataPoint1;
            DataPoint dataPoint2;
            if( xyValueArray.get(stt1).getX()<=xyValueArray.get(stt2).getX()){
                    dataPoint1 = new DataPoint(xyValueArray.get(stt1).getX(), xyValueArray.get(stt1).getY());
                    dataPoint2 = new DataPoint(xyValueArray.get(stt2).getX(), xyValueArray.get(stt2).getY());
            }
            else{
                    dataPoint1 = new DataPoint(xyValueArray.get(stt2).getX(), xyValueArray.get(stt2).getY());
                    dataPoint2 = new DataPoint(xyValueArray.get(stt1).getX(), xyValueArray.get(stt1).getY());
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                    dataPoint1,
                    dataPoint2
            });

            series.setColor(Color.GRAY);

            int exist=0;
            for(Series series1:mScatterPlot.getSeries()){
                if(series.getHighestValueX()==series1.getHighestValueX()
                        &&series.getHighestValueY()==series1.getHighestValueY()
                        &&series.getLowestValueX()==series1.getLowestValueX()
                        &&series.getLowestValueY()==series1.getLowestValueY()
                        &&series.getColor()==series1.getColor()) {
                    exist=1;
                    soduong++;
                    break;
                }
            }

            if(exist==0){
//                Log.v("series", series.getHighestValueX()
//                        +","+series.getHighestValueY()
//                        +","+series.getLowestValueX()
//                        +","+series.getLowestValueY());
                mScatterPlot.addSeries(series);

            }
        }
    }

    private void createPoint(){
        mScatterPlot = (GraphView) findViewById(R.id.scatterPlot);
        xySeries = new PointsGraphSeries<>();

        //generate two lists of random values, one for x and one for y.
        xyValueArray = new ArrayList<>();
        double start = -100;
        double end = 100;
        for(int i = 0; i<sodiem; i++){
            double randomX = new Random().nextDouble();
            double randomY = new Random().nextDouble();
            int x = (int) (start + (randomX * (end - start)));
            int y = (int) (start + (randomY * (end - start)));
            xyValueArray.add(new Point(x,y));
        }
        //sort it in ASC order
        xyValueArray = sortArray(xyValueArray);
        //add the data to the series
        for(int i = 0;i <xyValueArray.size(); i++){
            double x = xyValueArray.get(i).getX();
            double y = xyValueArray.get(i).getY();
           // int title=xyValueArray.get(i).getTitle();
            xySeries.appendData(new DataPoint(x,y),true, 1000);

//            Random rnd = new Random();
//            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            xySeries.setColor(Color.GREEN);
        }
    }

    private void getIntentFromStartActivity() {
        Intent intent=getIntent();
        sodiem= intent.getIntExtra("sodiem",0);
        soduong=intent.getIntExtra("soduong",0);
    }


    //tính trọng số đường nối
    private double lineValue(Series series){

        return Math.sqrt(Math.pow((series.getHighestValueX()-series.getLowestValueX()),2)+
                        Math.pow((series.getHighestValueY()-series.getLowestValueY()),2));
    }

    private void createScatterPlot( ) {
        //set some properties
        xySeries.setShape(PointsGraphSeries.Shape.POINT);
      //  xySeries.setColor(Color.BLUE);
        xySeries.setSize(10f);

        //set Scrollable and Scaleable
        mScatterPlot.getViewport().setScalable(true);
        mScatterPlot.getViewport().setScalableY(true);
        mScatterPlot.getViewport().setScrollable(true);
        mScatterPlot.getViewport().setScrollableY(true);

        //set manual x bounds
        mScatterPlot.getViewport().setYAxisBoundsManual(true);
        mScatterPlot.getViewport().setMaxY(150);
        mScatterPlot.getViewport().setMinY(-150);

        //set manual y bounds
        mScatterPlot.getViewport().setXAxisBoundsManual(true);
        mScatterPlot.getViewport().setMaxX(150);
        mScatterPlot.getViewport().setMinX(-150);

//        mScatterPlot.getLegendRenderer().setVisible(true);
//        mScatterPlot.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        mScatterPlot.addSeries(xySeries);//add point

    }

    private ArrayList<Point> sortArray(ArrayList<Point> array){
        /*
        //Sorts the xyValues in Ascending order to prepare them for the PointsGraphSeries<DataSet>
         */
        int factor = Integer.parseInt(String.valueOf(Math.round(Math.pow(array.size(),2))));
        int m = array.size()-1;
        int count = 0;
        Log.d(TAG, "sortArray: Sorting the XYArray.");

        while(true){
            m--;
            if(m <= 0){
                m = array.size() - 1;
            }
            Log.d(TAG, "sortArray: m = " + m);
            try{
                //print out the y entrys so we know what the order looks like
                //Log.d(TAG, "sortArray: Order:");
                //for(int n = 0;n < array.size();n++){
                //Log.d(TAG, "sortArray: " + array.get(n).getY());
                //}
                double tempY = array.get(m-1).getY();
                double tempX = array.get(m-1).getX();
                if(tempX > array.get(m).getX() ){
                    array.get(m-1).setY(array.get(m).getY());
                    array.get(m).setY(tempY);
                    array.get(m-1).setX(array.get(m).getX());
                    array.get(m).setX(tempX);
                }
                else if(tempY == array.get(m).getY()){
                    count++;
                    Log.d(TAG, "sortArray: count = " + count);
                }

                else if(array.get(m).getX() > array.get(m-1).getX()){
                    count++;
                    Log.d(TAG, "sortArray: count = " + count);
                }
                //break when factorial is done
                if(count == factor ){
                    break;
                }
            }catch(ArrayIndexOutOfBoundsException e){
                Log.e(TAG, "sortArray: ArrayIndexOutOfBoundsException. Need more than 1 data point to create Plot." +
                        e.getMessage());
                break;
            }
        }
        return array;
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


    //tạo ma trận trọng số
    private void createMatrix(){
        matrix=new double[sodiem][sodiem];
        for(int i=0;i<sodiem;i++){
            matrix[i][i]=0;
        }
        for(int i=0;i<sodiem;i++){
            for(int j=i+1;j<sodiem;j++)
            {
                //tạo đường nối 2 điểm
                DataPoint dataPoint1;
                DataPoint dataPoint2;
                if( xyValueArray.get(i).getX()<=xyValueArray.get(j).getX()){
                    dataPoint1 = new DataPoint(xyValueArray.get(i).getX(), xyValueArray.get(i).getY());
                    dataPoint2 = new DataPoint(xyValueArray.get(j).getX(), xyValueArray.get(j).getY());
                }
                else{
                    dataPoint1 = new DataPoint(xyValueArray.get(j).getX(), xyValueArray.get(j).getY());
                    dataPoint2 = new DataPoint(xyValueArray.get(i).getX(), xyValueArray.get(i).getY());
                }

                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                        dataPoint1,
                        dataPoint2
                });
                series.setColor(Color.GRAY);

                //kiểm tra đường nối đó có trên đồ thị hay k?
                for(Series series1:mScatterPlot.getSeries()){
                    if(series.getHighestValueX()==series1.getHighestValueX()
                            &&series.getHighestValueY()==series1.getHighestValueY()
                            &&series.getLowestValueX()==series1.getLowestValueX()
                            &&series.getLowestValueY()==series1.getLowestValueY()
                            &&series.getColor()==series1.getColor()) {
                        matrix[i][j]= matrix[j][i]= lineValue(series);
                        break;
                    }
                    else
                        matrix[i][j]=matrix[j][i]=0;
                }
            }
        }

        //log ra đúng
        for(int i=0;i<sodiem;i++) {
            for (int j = 0; j < sodiem; j++) {
                Log.v("a["+i+"]"+"["+j+"]=",matrix[i][j]+"");
            }
        }

    }

    public class CreateDoThi extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            createPoint();
            createScatterPlot();
            createLine();
            createMatrix();
            return null;
        }
    }

    public class CreateDijkstraTask extends AsyncTask<Integer, Void, ArrayList<Integer>> {
        Context context;
        ArrayList<Integer> list=new ArrayList<>();// luu tat ca cac điểm cha


        public CreateDijkstraTask(Context context) {
            this.context = context;
        }

        public ArrayList<Integer> getList() {
            return list;
        }

        public void setListchap(ArrayList<Integer> listchap) {
            this.list = list;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Integer> doInBackground(Integer... vitri) {
            back=new int[20];
            weight=new double[20];//lưu trọng số
            mark=new int[20];//đánh dấu đỉnh

            //khởi tạo
            for(int i=0;i<sodiem;i++){
                mark[i]=0;
                weight[i]=Double.MAX_VALUE;
                back[i]=-1;
            }
            //xuất phát tại đỉnh đầu tiên
            //vitri[0] là cái start trueyefn vào
            back[vitri[0]]=0;
            weight[vitri[0]]=0;

            //kiểm tra đồ thị có liên thông hay k
            int connect;
            do{
                connect=-1;
                double min=Double.MAX_VALUE;
                //lần lượt duyệt qua tất cả các đỉnh trong đồ thị
                for(int i=0;i<sodiem;i++){
                    if(mark[i]==0)// nếu đỉnh chưa được đánh dấu
                    {
                        //nếu tồn tại đường đi giữa đỉnh start và điểm i
                        //weight[i]: tổng trọng số từ đỉnh bắt đầu đến đỉnh đang xét
                        //weight[start]+matrix[start][i]: trọng số đang xét
                        if(matrix[vitri[0]][i]!=0 && weight[i]>weight[vitri[0]]+matrix[vitri[0]][i]){
                            weight[i]=weight[vitri[0]]+matrix[vitri[0]][i];
                            back[i]=vitri[0];//lưu đỉnh cha
                        }
                        if(min>weight[i]){
                            min=weight[i];
                            connect=i;
                        }
                    }
                }
                vitri[0]=connect;
                if(connect==-1)
                {
                    break;
                }
                mark[vitri[0]]=1;

            }while ( vitri[0]!=vitri[1]);

            for(int i = 0; i< back.length; i++)
                list.add(back[i]);
//            for(int i=0;i<list.size();i++)
//            {
//                Log.v("diem",list.get(i)+"");
//            }
            return list;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<Integer> list) {
            super.onPostExecute(list);
            //interfaceListChap.processFinish();
            // giờ phải vẽ như nào

            int finish = pointDijkstra[1];
            int start = list.get(finish);//sao start lai bang cai nay, lay gia tri no

            if(weight[pointDijkstra[1]]==Double.MAX_VALUE){
                    toastMessage("Không có đường đi");
            }
            else
            {
                do {
                    Log.v("start1", finish + "");
                    Log.v("start", start + "");
                    DataPoint dataPoint1;
                    DataPoint dataPoint2;
                    if (xyValueArray.get(start).getX() <= xyValueArray.get(finish).getX()) {
                        dataPoint1 = new DataPoint(xyValueArray.get(start).getX(), xyValueArray.get(start).getY());
                        dataPoint2 = new DataPoint(xyValueArray.get(finish).getX(), xyValueArray.get(finish).getY());
                    } else {
                        dataPoint1 = new DataPoint(xyValueArray.get(finish).getX(), xyValueArray.get(finish).getY());
                        dataPoint2 = new DataPoint(xyValueArray.get(start).getX(), xyValueArray.get(start).getY());
                    }

                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                            dataPoint1,
                            dataPoint2
                    });

                    series.setColor(Color.RED);
                    mScatterPlot.addSeries(series);

                    finish = start;
                    start = list.get(finish);
                } while (finish != pointDijkstra[0]);
                toastMessage("Độ Dài:"+weight[pointDijkstra[1]]);

            }

        }
    }


}




