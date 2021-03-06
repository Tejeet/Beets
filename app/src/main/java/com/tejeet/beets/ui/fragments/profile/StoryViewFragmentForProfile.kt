package com.tejeet.beets.ui.fragments.profile

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.exoplayer2.ExoPlaybackException
import com.tejeet.beets.app.MyApp
import com.tejeet.beets.ui.activities.main.viewmodel.MainViewModel
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.tejeet.beets.R
import com.tejeet.beets.data.modelDTO.StoriesData
import com.tejeet.beets.databinding.FragmentStoryViewBinding
import com.tejeet.beets.utils.*


class StoryViewFragmentForProfile : Fragment(R.layout.fragment_story_view) {
    private var storyUrl: String? = null
    private var storieData: StoriesData? = null

    private val args by navArgs<StoryViewFragmentForProfileArgs>()
    private var simplePlayer: SimpleExoPlayer? = null
    private var cacheDataSourceFactory: CacheDataSourceFactory? = null
    private val simpleCache = MyApp.simpleCache
    private var toPlayVideoPosition: Int = -1

    companion object {
        fun newInstance(storiesData: StoriesData) = StoryViewFragmentForProfile()
            .apply {
                arguments = Bundle().apply {
                    putParcelable(Constants.KEY_STORY_DATA, storiesData)
                }
            }
    }


    private var _binding: FragmentStoryViewBinding? = null
    private val mainViewModel: MainViewModel by viewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryViewBinding.inflate(inflater,container,false)

            storieData = args.storyData

        setData()
        return  binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun setData() {
        binding.textViewAccountHandle.setTextOrHide(value = storieData?.userName)
        binding.textViewVideoDescription.setTextOrHide(value = storieData?.storyDescription)
        binding.textViewMusicTitle.setTextOrHide(value = storieData?.musicCoverTitle)

        binding.imageViewOptionCommentTitle.text = "123"

        binding.imageViewOptionLikeTitle.text = "1222"
      //  storieData?.likesCount?.formatNumberAsReadableFormat()

        binding.imageViewProfilePic.loadCenterCropImageFromUrl(storieData?.userProfilePicUrl)

        binding.textViewMusicTitle.isSelected = true

        val simplePlayer = getPlayer()
        binding.playerViewStory.player = simplePlayer

        storyUrl = storieData?.storyUrl
        storyUrl?.let { prepareMedia(it) }

    }

    override fun onPause() {
        pauseVideo()
        super.onPause()
    }

    override fun onResume() {
        restartVideo()
        super.onResume()
    }

    override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }

    private val playerCallback: Player.EventListener? = object: Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            logError("onPlayerStateChanged playbackState: $playbackState")
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            super.onPlayerError(error)
        }
    }

    private fun prepareVideoPlayer() {
        simplePlayer = ExoPlayerFactory.newSimpleInstance(requireContext())
        cacheDataSourceFactory = simpleCache?.let {
            CacheDataSourceFactory(
                it,
                DefaultHttpDataSourceFactory(
                    Util.getUserAgent(requireContext(),
                    "exo"))
            )
        }
    }

    private fun getPlayer(): SimpleExoPlayer? {
        if (simplePlayer == null) {
            prepareVideoPlayer()
        }
        return simplePlayer
    }

    private fun prepareMedia(linkUrl: String) {
        logError("prepareMedia linkUrl: $linkUrl")

        val uri = Uri.parse(linkUrl)

        val mediaSource = cacheDataSourceFactory?.let { ProgressiveMediaSource.Factory(it).createMediaSource(uri) }

        if (mediaSource != null) {
            simplePlayer?.prepare(mediaSource, true, true)
        }
        simplePlayer?.repeatMode = Player.REPEAT_MODE_ONE
        simplePlayer?.playWhenReady = true
        playerCallback?.let { simplePlayer?.addListener(it) }

        toPlayVideoPosition = -1
    }

    private fun setArtwork(drawable: Drawable, playerView: PlayerView) {
        playerView.useArtwork = true
        playerView.defaultArtwork = drawable
    }

    private fun playVideo() {
        simplePlayer?.playWhenReady = true
    }

    private fun restartVideo() {
        if (simplePlayer == null) {
            storyUrl?.let { prepareMedia(it) }
        } else {
            simplePlayer?.seekToDefaultPosition()
            simplePlayer?.playWhenReady = true
        }
    }

    private fun pauseVideo() {
        simplePlayer?.playWhenReady = false
    }

    private fun releasePlayer() {
        simplePlayer?.stop(true)
        simplePlayer?.release()
    }
}