/*
 * Quicksort
 * and
 * Selection of order statistics
 * 
 * Alain Chen
 * 11/23/2016
 */


#include <cstdlib>
#include <iostream>
#include <vector>
#include <time.h>

using namespace std;

int randomizedSelect(vector<int> array, int p, int r, int i);
int randomizedPartition(vector<int>& array, int p, int r );
int partition(vector<int>& array,int p, int r );
void randomizedQuickSort(vector<int>& array, int p, int r );


int main(int argc, char** argv) {

    vector<int> list = {1,4,53,6,2,533,6,4,6};
    srand(time(NULL));
    
    int k = 4;
    
    //Finds the kth smallest integer in the list.
    cout << randomizedSelect(list, 0, list.size()-1, k) << endl;
    
    randomizedQuickSort(list,0,list.size()-1);
    cout << list.at(k-1) << endl;
    return 0;
}

//Find ith smallest integer in array starting at p, with last index r.
//Initially, you would call randomizedSelect(array, 0, array.size()-1, i);
int randomizedSelect(vector<int> array, int p, int r, int i)
{
    if(p==r)//Starting and end are the same.
        return array[p];
    int q = randomizedPartition(array,p,r);
    int k = q - p + 1;
    if(i==k)//We have a match
        return array[q];
    if(i<k)
        return randomizedSelect(array,p,q-1,i);
    return randomizedSelect(array,q+1,r,i-k);
}

//Chooses a random pivot to partition.
int randomizedPartition(vector<int>& array, int p, int r )
{
    int i = rand()%(r-p)+p;
    int temp = array[r];
    array[r] = array[i];
    array[i] = temp;
    return partition(array,p,r);
}

//Partitions a given list with indexes from p to r, inclusive.
//Always use the last index r as the pivot.
int partition(vector<int>& array, int p, int r )
{
    int x = array[r];
    int i = p-1;
    int temp;
    for(int j=p;j<r;j++)
    {
        if(array[j]<=x)
        {
            i++;
            temp = array[j];
            array[j] = array[i];
            array[i] = temp;
        }     
    }
    
    temp = array[i+1];
    array[i+1] = array[r];
    array[r] = temp;
    return i+1;
}

//Quicksort with randomized partition.
void randomizedQuickSort(vector<int>& array, int p, int r )
{
    if(p>=r)
        return;
    int q=randomizedPartition(array,p,r);
    randomizedQuickSort(array,p,q-1);
    randomizedQuickSort(array,q+1,r);
}

