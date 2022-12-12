package com.androiddevs.mvvmnewsapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.ui.models.Article
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_article_preview.view.*


/**
 * important note for diffUtil and notifyDataSetChanged
 * for notifyDataSetChanged if used , it will update all the recyclerView items even the not changed ones
 *
 * but DiffUtil is better as it only update the changed items
 *
 */



class NewsAdapter :RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>(){

    inner class ArticleViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)


    val differCallBack=object :DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            //we're comparing url which will be unique for the item here for the old and the new item
            //to check if the old item is the same as new new item or not
            return oldItem.url ==newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem==newItem
        }
    }

    fun clearList(){

    }

    //creating variable that takes the recyclerView adapter created and the differCallback created
    val differ=AsyncListDiffer(this,differCallBack)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_article_preview,parent,false)
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article=differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(ivArticleImage)
            tvSource.text=article.source.name
            tvTitle.text=article.title
            tvDescription.text=article.description
            tvPublishedAt.text=article.publishedAt
            //calling the function setOnItemClickListener we created


            //set on click listener is triggered for this item in the recyclerView
            //when clicking on an item , will do the implementation inside it which is calling onItemClickListener
            //the implementation is to call the variable of type function onItemClickListener
            //so inside setOnClickListener will call the onItemClickListener and give it some properties that is
            //the parameter of the onItemClickListener which is function of type lambda and takes article and inside this let
            //we have reference to the parameter by using it so will call it(article)
            //meaning that our variable now which is onItemClickListener is function of type lambda that will take parameter article of type article
            //and will return nothing and this will be called only if the onItemClickListener is not equal to null , thanks to let block
            setOnClickListener {
                onItemClickListener?.let {
                    it(article) }
            }
        }
    }


    /**
     * the downward code is an example of creating a listener
     * we are creating listener variable of type lambda function that takes parameter of type Article
     * and returns nothing
     *
     * then we create a function that takes parameter of type also lambda function and also this lambda function
     *
     * then we call the function setOnItemClickListener
     */

    //variable of type lambda function that takes variable article and returns nothing and this variable equals null for now
    private var onItemClickListener:((Article)->Unit)?=null

    //function that takes parameter of type function and this function  takes parameter of type article and
    //returns nothing
    //inside this function we set the function listener we created with the function listener passed
    fun setOnItemClickListener(listener:(Article)->Unit){
        //here the onItemClickListener will take the value of passed listener meaning that the trailing lambda passed onItemClickListener equalized by the
        //the trailing lambda passed when calling setOnItemClickListener and the onItemClickListener or listener is variable of type lambda function , taking
        //article as parameter and this paramter already passed when clicking on the item in recyclerview and this happens here
        /**
         *             setOnClickListener {
        onItemClickListener?.let {
        it(article) }
        }
         */
        onItemClickListener=listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}