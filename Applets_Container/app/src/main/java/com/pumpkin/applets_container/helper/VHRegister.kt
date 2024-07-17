package com.pumpkin.applets_container.helper

import androidx.viewbinding.ViewBinding
import com.pumpkin.applets_container.databinding.*
import com.pumpkin.applets_container.view.vh.*
import com.pumpkin.mvvm.adapter.BaseVH
import com.pumpkin.mvvm.adapter.TypeHelper

object VHRegister {

    fun register() {

//        TypeHelper.register(CarouselItemVH.TYPE) { layoutInflater, context, parent, requestManager ->
//            CarouselItemVH(VhCarouselItemBinding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
//        }

//        TypeHelper.register(CarouselHorizontalVH.TYPE) { layoutInflater, context, parent, requestManager ->
//            CarouselHorizontalVH(VhCommonHorizontalBinding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
//        }

        TypeHelper.register(BigCardVH.TYPE) { layoutInflater, context, parent, requestManager ->
            BigCardVH(VhBigCardBinding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
        }

        TypeHelper.register(RecentHorizontalVH.TYPE) { layoutInflater, context, parent, requestManager ->
            RecentHorizontalVH(VhCommonHorizontalBinding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
        }

        TypeHelper.register(RecentItemVH.TYPE) { layoutInflater, context, parent, requestManager ->
            RecentItemVH(VhRecentItemBinding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
        }

        TypeHelper.register(EditorPickHorizontalVH.TYPE) { layoutInflater, context, parent, requestManager ->
            EditorPickHorizontalVH(VhCommonHorizontalBinding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
        }

        TypeHelper.register(TitleVH.TYPE) { layoutInflater, context, parent, requestManager ->
            TitleVH(VhTitleBinding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
        }

        TypeHelper.register(WordCardStyle1VH.TYPE) { layoutInflater, context, parent, requestManager ->
            WordCardStyle1VH(VhWordCardStyle1Binding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
        }

        TypeHelper.register(OfflineCardStyle1VH.TYPE) { layoutInflater, context, parent, requestManager ->
            OfflineCardStyle1VH(VhOfflineCardStyle1Binding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
        }

        TypeHelper.register(EditorPickItemVH.TYPE) { layoutInflater, context, parent, requestManager ->
            EditorPickItemVH(VhEditorPickItemBinding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
        }

        TypeHelper.register(SearchItemVH.TYPE) { layoutInflater, context, parent, requestManager ->
            SearchItemVH(VhSearchItemBinding.inflate(layoutInflater), context, requestManager) as BaseVH<Any, ViewBinding>
        }


    }
}