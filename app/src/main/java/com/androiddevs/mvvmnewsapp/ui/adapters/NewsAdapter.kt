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


    private val differCallBack=object :DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            //we're comparing url which will be unique for the item here for the old and the new item
            //to check if the old item is the same as new new item or not
            return oldItem.url ==newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem==newItem
        }
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

            setOnItemClickListener {
                //checking if onItemClickListener= null or not , by using the let code that executes if the value not null
                //if = null do nothing , but if not , pass the article to this variable of type function takes parameter of type article , defined downwards
                onItemClickListener?.let {
                    it(article)
                }
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

    //variable of type lambda function that takes variable article and returns nothing
    private var onItemClickListener:((Article)->Unit)?=null

    //function that takes parameter of type function and this function  takes parameter of type article and
    //returns nothing
    //inside thi function we set the function listener we created with the function listener passed
    fun setOnItemClickListener(listener:(Article)->Unit){
        onItemClickListener=listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}